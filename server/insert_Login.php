<?php
    //유진
    session_start();
    if(isset($_COOKIE['userID'])){

    $loginID = trim($_COOKIE['userID']);
    $keyword = $_POST['key'];
    $context = $_POST['context'];
    $latitude = $_POST['latitude'];
    $longitude = $_POST['longitude'];

    $conn = mysqli_connect('localhost', 'root', 'djsxhr12');
    mysqli_select_db($conn, 'app');

    mysqli_set_charset($conn,"UTF8");
    $result = mysqli_query($conn, "SELECT * FROM keyword_info WHERE keyword='".$keyword."'");
    $row = mysqli_fetch_array($result, MYSQL_BOTH);
    if(is_null($row)){
        $sql = 'INSERT INTO keyword_info (keyword, count_keyword) VALUES ("'.$keyword.'", "1")';
        mysqli_set_charset($conn,"UTF8");
        mysqli_query($conn,$sql);
    }
    else{
        $count = $row[2] + 1;
        $result = mysqli_query($conn, "UPDATE keyword_info SET count_keyword='".$count."' WHERE keyword='".$keyword."'");
    }

    $sql = 'INSERT INTO context_info (context_key, requirement, latitude, longitude, user_email, created, created_date) VALUES ("'.$keyword.'","'.$context.'","'.$latitude.'","'.$longitude.'","'.$loginID.'",curtime(), now())';
    mysqli_set_charset($conn,"UTF8");
    mysqli_query($conn,$sql);

       mysqli_set_charset($conn,"UTF8");
       $result = mysqli_query($conn, "SELECT * FROM user_info WHERE user_email='".$loginID."'");
       $row = mysqli_fetch_array($result, MYSQL_BOTH);
       $count = $row['count'] + 1;

        $sql = "UPDATE user_info SET count='$count' WHERE user_email='$loginID'";
        mysqli_set_charset($conn,"UTF8");
        mysqli_query($conn,$sql);

    }
    

?>
