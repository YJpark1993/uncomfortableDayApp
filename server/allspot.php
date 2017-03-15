<?php

$conn = mysqli_connect('localhost', 'root', 'djsxhr12');
mysqli_select_db($conn, 'app');
mysqli_set_charset($conn,"UTF8");

$res  = mysqli_query($conn, "SELECT * FROM context_info");

$result = array();

while($row = mysqli_fetch_array($res)){
  array_push($result,
    array('latitude'=>$row[3],'longitude'=>$row[4]));
}

echo json_encode(array("result"=>$result));

mysqli_close($con);
?>
