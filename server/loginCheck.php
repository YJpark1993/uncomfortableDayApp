<?php
// 상기꺼
  session_start();
  if(isset($_POST['loginID']) && !empty($_POST['loginID']) &&
  isset($_POST['loginPW']) && !empty($_POST['loginPW'])){
    $loginID = trim($_POST['loginID']);
    $loginPW = trim($_POST['loginPW']);


    $conn = mysqli_connect('localhost', 'root', 'djsxhr12');
    mysqli_select_db($conn, 'app');
    $sql = "SELECT * FROM user_info WHERE user_email='".$loginID."' AND user_pw = '".$loginPW."'";
    mysqli_set_charset($conn,"UTF8");
    $result = mysqli_query($conn,$sql);

    if($result->num_rows){
      $row = mysqli_fetch_assoc($result);
      $_SESSION['userID'] = $row['user_email'];
      $_SESSION['userPW'] = $row['user_pw'];
      $_SESSION['ip'] = $_SERVER['REMOTE_ADDR']; // 사용자 IP주소
      $_SESSION['ua'] = $_SERVER['HTTP_USER_AGENT']; // 기기 주소
      setcookie('userID', $loginID , time()+3600 * 24, '/' );
      setcookie('userPW', $loginPW , time()+3600 * 24, '/' );
      echo "success";
    } else{
      echo "fail";
    }

  }


?>
