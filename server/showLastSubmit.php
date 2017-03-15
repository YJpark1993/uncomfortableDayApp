<?php
//유진
 if(isset($_COOKIE['userID'])){

    $loginID = trim($_COOKIE['userID']);
  $conn = mysqli_connect('localhost', 'root', 'djsxhr12');
  mysqli_select_db($conn, 'app');



  $sql = "SELECT context_key, created, flag_ok, created_date FROM context_info WHERE user_email='".$loginID."'";
  mysqli_query($conn,"SET NAMES 'utf8'");
  $result = mysqli_query($conn,$sql);
  $output = array();
  while($row = mysqli_fetch_assoc($result)){
    array_push($output,
    array('context_key'=>$row['context_key'],'created'=>$row['created'],'flag_ok'=>$row['flag_ok'],'created_date'=>$row['created_date']));
  }

   echo json_encode(array("output"=>$output));

}


?>
