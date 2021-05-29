<?php
session_start();

if (isset($_GET['token'])) {
    $_SESSION['token'] = $_GET['token'];
}
?>

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
        padding: 16px 0;
        padding-bottom: 20px;
    }

    h3 {
        padding: 8px 0;
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
                if (isset($_GET['landingType']) && isset($_SESSION['token'])) {
                    if ($_GET['landingType'] == "verify") {
                        echo "<h1>Email Verification</h1>";

                        $crl = curl_init();
                        curl_setopt($crl, CURLOPT_URL, "https://api.helpupil.at/v1/auth/verify-email?token=" . $_SESSION['token']);
                        curl_setopt($crl, CURLOPT_RETURNTRANSFER, true);

                        $result = curl_exec($crl);

                        if ($result === false) {
                            echo "Could not reach the API";
                        } else {
                            $http_response_code = curl_getinfo($crl, CURLINFO_HTTP_CODE);
                            if ($http_response_code == 204) {
                                echo "<h2>You are now verified!</h2>";
                                echo "<h3>Thank you for registering!</h3>";
                            } else {
                                $obj = json_decode($result);
                                echo "<h2>Verification failed!</h2>";
                                echo "<h3>" . $obj->{'code'} . " Error</h3>";
                                echo "<h3>Message: " . $obj->{'message'} . "</h3>";
                            }
                        }

                        curl_close($crl);

                        session_unset();
                        session_destroy();
                    } else if ($_GET['landingType'] == "resetPassword" && isset($_SESSION['token'])) {
                        echo '
                        <h1>Reset Password</h1><br>
                        <form action="' . $_SERVER['PHP_SELF'] . '" method="POST">
                            <div class="inputDiv">
                                <label for="newPassword">New Password:</label>
                                <input type="password" name="password" id="newPasswordIn">
                            </div>
                            <button id="setNewPassword" onclick="submit()">Confirm</button>
                        </form>
                        ';
                    }
                }


                if (isset($_POST['password']) && isset($_SESSION['token'])) {
                    echo  "<h1>Reset Password</h1><br>";

                    $data = array(
                        'password' => $_POST['password'],
                    );

                    $post_data = json_encode($data);

                    $crl = curl_init('https://api.helpupil.at/v1/auth/reset-password?token=' . $_SESSION['token']);
                    curl_setopt($crl, CURLOPT_RETURNTRANSFER, true);
                    curl_setopt($crl, CURLINFO_HEADER_OUT, true);
                    curl_setopt($crl, CURLOPT_POST, true);
                    curl_setopt($crl, CURLOPT_POSTFIELDS, $post_data);

                    curl_setopt(
                        $crl,
                        CURLOPT_HTTPHEADER,
                        array(
                            'Content-Type: application/json'
                        )
                    );

                    $result = curl_exec($crl);

                    if ($result === false) {
                        echo "Could not reach the API";
                    } else {
                        $http_response_code = curl_getinfo($crl, CURLINFO_HTTP_CODE);
                        if ($http_response_code == 204) {
                            echo "<h2>Reset successful!</h2>";
                        } else {
                            $obj = json_decode($result);
                            echo "<h2>Reset failed!</h2>";
                            echo "<h3>" . $obj->{'code'} . " Error</h3>";
                            echo "<h3>Message: " . $obj->{'message'} . "</h3>";
                        }
                    }
                    curl_close($crl);

                    session_unset();
                    session_destroy();
                }
                ?>
            </div>
        </div>
    </div>
</body>

</html>