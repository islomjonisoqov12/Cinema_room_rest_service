<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Countdown Timer</title>
    <style>
        @import url("https://fonts.googleapis.com/css2?family=Poppins:wght@200;400;600&display=swap");

        * {
            box-sizing: border-box;
        }

        body {
            background-size: cover;
            background-position: center center;
            display: flex;
            flex-direction: column;
            align-items: center;
            min-height: 100vh;
            font-family: "Poppins", sans-serif;
            margin: 0;
        }

        h1 {
            font-weight: normal;
            font-size: 4rem;
            margin-top: 5rem;
        }

        .countdown-container {
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
        }

        .big-text {
            font-weight: bold;
            font-size: 6rem;
            line-height: 1;
            margin: 1rem 2rem;
        }

        .countdown-el {
            text-align: center;
        }

        .countdown-el span {
            font-size: 1.3rem;
        }
    </style>
</head>
<body>
<h1>End of Year 2023</h1>

<div class="countdown-container">
    <div class="countdown-el days-c">
        <p class="big-text" id="days">0</p>
        <span>days</span>
    </div>
    <div class="countdown-el hours-c">
        <p class="big-text" id="hours">0</p>
        <span>hours</span>
    </div>
    <div class="countdown-el mins-c">
        <p class="big-text" id="mins">0</p>
        <span>mins</span>
    </div>
    <div class="countdown-el seconds-c">
        <p class="big-text" id="seconds">0</p>
        <span>seconds</span>
    </div>
</div>
</body>
</html>