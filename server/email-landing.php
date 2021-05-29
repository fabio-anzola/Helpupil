<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Helpupil</title>
</head>

<body>

    <?php

    if (isset($_GET['landingType']) && isset($_GET['token'])) {
        if ($_GET['landingType'] == "verify") {
            $ch = curl_init();
            curl_setopt($ch, CURLOPT_URL, "https://api.helpupil.at/v1/auth/verify-email?token=" . $_GET['token']);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

            $data = curl_exec($ch);
            curl_close($ch);

            echo $data;
        } else if ($_GET['landingType'] == "resetPassword") {
        }
    }

    ?>

</body>

</html>