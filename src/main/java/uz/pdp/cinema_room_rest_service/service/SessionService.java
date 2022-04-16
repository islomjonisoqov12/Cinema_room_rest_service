package uz.pdp.cinema_room_rest_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.cinema_room_rest_service.dto.HallDto;
import uz.pdp.cinema_room_rest_service.dto.AnnouncementDto;
import uz.pdp.cinema_room_rest_service.dto.SessionDto;
import uz.pdp.cinema_room_rest_service.dto.SessionTimeDto;
import uz.pdp.cinema_room_rest_service.model.*;
import uz.pdp.cinema_room_rest_service.payload.ApiResponse;
import uz.pdp.cinema_room_rest_service.projection.HallBusyDate;
import uz.pdp.cinema_room_rest_service.projection.MovieSessionProjection;
import uz.pdp.cinema_room_rest_service.projection.SessionProjection;
import uz.pdp.cinema_room_rest_service.repository.*;

import javax.transaction.Transactional;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.sql.Date;
import java.util.*;

@Service
@Transactional
public class SessionService {

    @Autowired
    MovieAnnouncementRepository sessionRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    HallRepository hallRepository;

    @Autowired
    SessionDateRepository dateRepository;

    @Autowired
    SessionTimesRepository timeRepository;

    @Autowired
    MovieSessionRepository movieSessionRepository;

    @Autowired
    MovieAnnouncementRepository movieAnnouncementRepository;

    public ApiResponse getAllSession(int page, int size, boolean expired) {
        Page<SessionProjection> allSession = sessionRepository.getAllSession(PageRequest.of(page, size), expired);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(allSession);
        apiResponse.setMessage("success");
        apiResponse.setSuccess(true);
        return apiResponse;
    }

    public ApiResponse getById(UUID id) {
        MovieSessionProjection sessionById = sessionRepository.getSessionById(id);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("success");
        apiResponse.setData(sessionById);
        apiResponse.setSuccess(true);
        return apiResponse;

    }

    public ResponseEntity<ApiResponse> saveSession(AnnouncementDto announcementDto) {
        List<Map<String, Object>> responseList = new ArrayList<>();
        ApiResponse response = new ApiResponse();
        try {
            Optional<Movie> movieOp = movieRepository.findById(announcementDto.getMovieId());
            if (movieOp.isPresent()) {
                List<HallDto> hallDtoList = announcementDto.getHalls();

                MovieAnnouncement movieAnnouncement = new MovieAnnouncement();
                movieAnnouncement.setMovie(movieOp.get());
                movieAnnouncement.setActive(announcementDto.isActive());
                movieAnnouncementRepository.save(movieAnnouncement);
                if (hallDtoList != null && hallDtoList.size() > 0) {
                    for (HallDto hallDto : hallDtoList) {

                        Optional<Hall> hallOp = hallRepository.findById(hallDto.getHallId());
                        if (hallOp.isPresent()) {
                            Hall hall = hallOp.get();
                            List<SessionDates> sessionDates = getSessionDate(hallDto.getStartDate().toLocalDate(), hallDto.getEndDate().toLocalDate());
                            for (SessionDates sessionDate : sessionDates) {
                                List<SessionTime> startTimes = getSessionTimes(hallDto.getStartTimes());
                                for (SessionTime startTime : startTimes) {

                                    Map<String, Object> hasAnySession = movieSessionRepository.hasAnySession(sessionDate.getDate(), startTime.getStartTime(), findEndTimeByStartTime(hallDto.getStartTimes(), startTime), hallDto.getHallId());
                                    if (hasAnySession.size() == 0) {
                                        MovieSession movieSession = new MovieSession();
                                        movieSession.setHall(hall);
                                        movieSession.setDate(sessionDate);
                                        movieSession.setMovieAnnouncement(movieAnnouncement);
                                        movieSession.setStartTime(startTime);
                                        movieSession.setEndTime(findEndTimeByStartTime(hallDto.getStartTimes(), startTime));
                                        movieSessionRepository.save(movieSession);
                                        movieAnnouncement.addMovieSession(movieSession);
                                    }else
                                    {
                                        Map<String , Object> map = new HashMap<>();
                                        map.put("date", hasAnySession.get("date"));
                                        map.put("startTime", hasAnySession.get("startTime"));
                                        map.put("endTime", hasAnySession.get("endTime"));
                                        map.put("movieTitle", hasAnySession.get("movieTitle"));
                                        responseList.add(map);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if(responseList.size()>0) throw new IllegalArgumentException("session is already exists");
            response.setData(responseList);
            response.setSuccess(true);
            response.setMessage("movie announcement successfully saved");
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setData(responseList);
            response.setSuccess(false);
        }
        return ResponseEntity.ok(response);
    }

    private Time findEndTimeByStartTime(List<SessionTimeDto> startTimes, SessionTime startTime) {
        return startTimes.stream().filter(sessionTimeDto -> sessionTimeDto.getStartTime().equals(startTime.getStartTime())).findFirst().get().getEndTime();
    }

    private List<SessionTime> getSessionTimes(List<SessionTimeDto> startTimes) {
        List<SessionTime> sessionTimes = new ArrayList<>();
        for (SessionTimeDto startTime : startTimes) {
            Optional<SessionTime> byStartTime = timeRepository.findByStartTime(startTime.getStartTime());
            if (byStartTime.isPresent()) {
                sessionTimes.add(byStartTime.get());
            } else {
                SessionTime sessionTime = new SessionTime(null, startTime.getStartTime(), startTime.getSessionAdditionalFeeInPer());
                timeRepository.save(sessionTime);
            }
        }
        return sessionTimes;
    }

    private List<SessionDates> getSessionDate(LocalDate startDate, LocalDate endDate) {
        List<SessionDates> sessionDates = new ArrayList<>();
        List<LocalDate> dates = getDates(startDate, endDate);
        for (LocalDate date : dates) {
            SessionDates byDate = dateRepository.findByDate(date);
            if (byDate == null) {
                byDate = new SessionDates(null, date);
                dateRepository.save(byDate);
            }
            sessionDates.add(byDate);
        }
        return sessionDates;
    }

    private List<LocalDate> getDates(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate date = startDate;
        while (date.isBefore(endDate.plusDays(1))) {//endDate.plus(1, ChronoUnit.DAYS))
            dates.add(date);
            date = date.plusDays(1);
        }
        return dates;
    }

    public ResponseEntity<ApiResponse> checkBusyDateTimeHalls(Date start, Date end, UUID hallId) {
        if (end == null) end = start;
        ApiResponse response = new ApiResponse();
        try {
            List<HallBusyDate> busyDateTime = hallRepository.getBusyDateTime(start, end, hallId);
            response.setMessage("success");
            response.setData(busyDateTime);
            response.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    public HttpEntity<ApiResponse> deleteSession(UUID id) {
        ApiResponse response = new ApiResponse();
        try {
            movieSessionRepository.deleteById(id);
            response.setMessage("successfully deleted");
            response.setSuccess(true);
        }catch (Exception e){
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    public HttpEntity<ApiResponse> deleteByDateTime(Timestamp start, Timestamp end) {
        ApiResponse response = new ApiResponse();
        try {
            if (start == null) throw new NullPointerException("you must input start datetime");
            if (end != null) {
                List<UUID> list = movieSessionRepository.deleteByDateTime(start, end);
                for (UUID uuid : list) {
                    System.out.println(uuid);
                }
                movieSessionRepository.deleteAllById(list);
                response.setData(null);
                response.setSuccess(true);
                response.setMessage("successfully deleted");
            }
        }catch (Exception e){
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setSuccess(false);
        }
        return ResponseEntity.ok(response);
    }

    public HttpEntity<ApiResponse> saveSession(SessionDto sessionDto, UUID userId) {
        ApiResponse response = new ApiResponse();
        Map<String, Object> map = new HashMap<>();
        try {
            LocalDate startDate = sessionDto.getStartDateTime().toLocalDateTime().toLocalDate();
            SessionDates sessionDate = dateRepository.findByDate(startDate);
            if(sessionDate==null){
                sessionDate = new SessionDates(null,startDate);
                dateRepository.save(sessionDate);
            }
            Time startTime = new Time(sessionDto.getStartDateTime().getTime());
            Optional<MovieAnnouncement> optionalMovieAnnouncement = movieAnnouncementRepository.findById(sessionDto.getMovieAnnouncementId());
            if(!optionalMovieAnnouncement.isPresent()) throw new ResourceNotFoundException("movie announcement does not exists!");
            Optional<Hall> optionalHall = hallRepository.findById(sessionDto.getHallId());
            if (!optionalHall.isPresent()) throw new ResourceNotFoundException("hall does not exists!");
            Optional<SessionTime> optionalSessionTime = timeRepository.findByStartTime(startTime);
            SessionTime sessionTime = null;
            if (!optionalSessionTime.isPresent()) {
                sessionTime = new SessionTime(null,startTime,timeRepository.findAdditionalFeeByTime(startTime));
                timeRepository.save(sessionTime);
            }else sessionTime = optionalSessionTime.get();
            Timestamp endDateTime = sessionDto.getEndDateTime();
            if(endDateTime == null) endDateTime = timeRepository.findEndTimeByMovieIdAndStartDateTime(optionalMovieAnnouncement.get().getMovie().getId(), startTime, startDate);
            map = movieSessionRepository.hasAnySession(startDate, startTime, new Time(endDateTime.getTime()), sessionDto.getHallId());
            if (map.size()>0) throw new IllegalArgumentException("the session could not be added. Because the selected hall is not empty at a given time");
            MovieSession movieSession = new MovieSession();
            movieSession.setDate(sessionDate);
            movieSession.setMovieAnnouncement(optionalMovieAnnouncement.get());
            movieSession.setHall(optionalHall.get());
            movieSession.setStartTime(sessionTime);
            movieSession.setEndTime(new Time(endDateTime.getTime()));
            movieSession.setCreatedBy(userId);
            movieSession.setUpdatedBy(userId);
            movieSessionRepository.save(movieSession);
            response.setMessage("successfully created");
            response.setSuccess(true);
        }catch (Exception e){
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setSuccess(false);
            response.setData(map);
        }
        return ResponseEntity.ok(response);
    }
}
