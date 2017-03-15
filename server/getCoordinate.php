<?php
  $conn = mysqli_connect('localhost', 'root', 'djsxhr12');
  mysqli_select_db($conn, 'app');



  $sql = 'SELECT context_key,latitude,longitude FROM context_info';
  mysqli_query($conn,"SET NAMES 'utf8'");
  $result = mysqli_query($conn,$sql);
  $output = array();
  while($row = mysqli_fetch_assoc($result)){
    array_push($output,
    array('context_key'=>$row['context_key'],'latitude'=>$row['latitude'],'longitude'=>$row['longitude']));
  }

  $sql = 'SELECT context_key_road,latitude,longitude,created FROM contextRoad_info';
  mysqli_query($conn,"SET NAMES 'utf8'");
  $result = mysqli_query($conn,$sql);
  $outputRoad = array();
  while($row = mysqli_fetch_assoc($result)){
    array_push($outputRoad,
    array('context_key_road'=>$row['context_key_road'],'latitude'=>$row['latitude'],'longitude'=>$row['longitude'],'curtime'=>$row['created']));
  }
  echo json_encode(array("output"=>$output,"outputRoad"=>$outputRoad));

?>
