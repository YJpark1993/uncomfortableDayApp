<?php
// 유진
  session_start();
  if(isset($_COOKIE['userID']) || isset($_COOKIE['userPW'])){
    $loginID = trim($_COOKIE['userID']);
    $loginPW = trim($_COOKIE['userPW']);

    $conn = mysqli_connect('localhost', 'root', 'djsxhr12');
    mysqli_select_db($conn, 'app');
    $sql = "SELECT * FROM user_info WHERE user_email='".$loginID."' AND user_pw = '".$loginPW."'";
    mysqli_set_charset($conn,"UTF8");
    $result = mysqli_query($conn,$sql);
     
    $row = mysqli_fetch_array($result,MYSQL_BOTH);
     echo $row['count'];
     echo ";";
     echo $row['score'];
}
  

?>
