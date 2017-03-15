<?php
    $keyRoad = $_POST['keyRoad'];
    $latitude = $_POST['latitude'];
    $longitude = $_POST['longitude'];
    $conn = mysqli_connect('localhost', 'root', 'djsxhr12');
    mysqli_select_db($conn, 'app');

    mysqli_set_charset($conn,"UTF8");
    $result = mysqli_query($conn, "SELECT * FROM keywordRoad_info WHERE keyword_road='".$keyRoad."'");
    $row = mysqli_fetch_array($result, MYSQL_BOTH);
    if(is_null($row)){
        $sql = 'INSERT INTO keywordRoad_info (keyword_road, keyword_road_count) VALUES ("'.$keyRoad.'", "1")';
        mysqli_set_charset($conn,"UTF8");
        mysqli_query($conn,$sql);
    }
    else{
        $count = $row[2] + 1;
        $result = mysqli_query($conn, "UPDATE keywordRoad_info SET keyword_road_count='".$count."' WHERE keyword_road='".$keyRoad."'");
    }

    $sql = 'INSERT INTO contextRoad_info (context_key_road, latitude, longitude, created, created_date) VALUES ("'.$keyRoad.'","'.$latitude.'","'.$longitude.'",curtime(), now())';
    mysqli_set_charset($conn,"UTF8");
    mysqli_query($conn,$sql);

?>
