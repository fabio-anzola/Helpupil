<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Helpupil</title>

    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">
</head>

<style>
    * {
        font-family: 'Roboto', sans-serif;
        color: #253345;
        margin: 0;
        padding: 0;
    }

    body {
        background-color: #e7f1fe;
        height: 100vh;
        display: flex;
        justify-content: space-around;
        flex-direction: row;
    }

    div.vertical-centering-wrapper {
        display: flex;
        justify-content: center;
        margin-top: auto;
        margin-bottom: auto;
    }

    div.centered-wrapper {
        background-color: white;
        display: flex;
        justify-content: center;
        flex-wrap: wrap;
        padding: 50px;
        margin-left: 25px;
        margin-right: 25px;
        border-radius: 10px;

        -webkit-box-shadow: 5px 5px 12px 3px rgba(0, 0, 0, 0.12);
        -moz-box-shadow: 5px 5px 12px 3px rgba(0, 0, 0, 0.12);
        box-shadow: 5px 5px 12px 3px rgba(0, 0, 0, 0.12);
    }

    h1,
    h2,
    h3 {
        text-align: center;
    }

    h1 {
        padding-bottom: 25px;
    }

    h2 {
        padding: 12px 0;
    }

    h3 {
        padding-top: 8px 0;
    }

    label,
    input {
        display: block;
        margin: 10px 0;
    }

    div.inputDiv {
        width: 100%;
    }

    input {
        width: 94%;
        font-size: 13pt;
        border: none;
        background-color: #e8ebef;
        
        border-radius: 4px;
        padding: 7px;
    }

    input:focus {
        outline: none;
    }

    button#setNewPassword {
        margin-top: 25px;
        border: none;
        background-color: #1676f3;
        color: white;
        font-weight: bold;
        font-size: 12pt;
        padding: 10px;
        margin-top: 35px;
        border-radius: 4px;
        width: 100%;
    }

    button#setNewPassword:hover {
        background-color: #2d83f4;
        cursor: pointer;
    }
</style>

<body>
    <div class="vertical-centering-wrapper">
        <div class="centered-wrapper">
            <div>
                <?php
                if (isset($_GET['landingType']) && isset($_GET['token'])) {
                    if ($_GET['landingType'] == "verify") {
                        $ch = curl_init();
                        curl_setopt($ch, CURLOPT_URL, "https://api.helpupil.at/v1/auth/verify-email?token=" . $_GET['token']);
                        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

                        $data = curl_exec($ch);
                        curl_close($ch);

                        if (empty($data)) {
                            echo "<h1>You are now verified!</h1>";
                            echo "<h3>Thank you for registering!</h3>";
                        } else {
                            $obj = json_decode($data);
                            echo "<h1>Verification failed!</h1>";
                            echo "<h2>" . $obj->{'code'} . " Error</h2>";
                            echo "<h3>Message: " . $obj->{'message'} . "</h3>";
                        }
                    } else if ($_GET['landingType'] == "resetPassword") {
                    }
                }

                ?>

                <h1>Reset Password</h1>
                <br>
                <div class="inputDiv">
                    <label for="newPassword">New Password:</label>
                    <input type="password" name="newPassword" id="newPasswordIn">
                </div>
                <br>
                <div class="inputDiv">
                    <label for="confirmNewPassword">Confirm new Password:</label>
                    <input type="password" name="confirmNewPassword" id="newPasswordIn">
                </div>
                <button id="setNewPassword">Confirm</button>
            </div>
        </div>
    </div>
</body>

</html>