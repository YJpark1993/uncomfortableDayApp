<?php

$conn = mysqli_connect('localhost', 'root', 'djsxhr12');
mysqli_select_db($conn, 'app');
mysqli_set_charset($conn,"UTF8");

$res  = mysqli_query($conn, "SELECT * FROM context_info");

$result = array();

while($row = mysqli_fetch_array($res)){
  array_push($result,
    array('key'=>$row[1],'req'=>$row[2],'lat'=>$row[3],'lng'=>$row[4],'email'=>$row[5],'flag'=>$row[6],'day'=>$row[7],'time'=>$row[8],'count'=>0));
}
?>

<!DOCTYPE html>
<html>
  <head>
  <meta charset="UTF-8">
    <style type="text/css">
      html, body { height: 100%; margin: 0; padding: 0; font-size: 10px; }
      #map { height: 100%; }
      #popup {
        margin-top: 10px;
        width: 250px;
        height: auto;
        position: absolute;
        z-index: 1;
        float: right;
        display: none;
        background-color: #fff;
        border-radius: 0 10px 0 10px;
        background-color: white;
        color: black;
        border: 2px solid #4CAF50; /* Green */
        padding: 0 0 0 20px;
      }
      #bt_close {
        background-color: white;
        color: black;
        border: 2px solid #000; /* Green */
        border-radius: 5px;
        font-size: 20px;
        margin:13px 15px 0 0;
      }
    </style>
  </head>
  <body>
      <div id="popup">
        <span class="button b-close" style="float:right;"><button id="bt_close">X</button></span><br>
        <div id="content"></div>
    </div>
      <div id="map"></div>
  </body>
</html>

 <script type="text/javascript">
      var map;
      var neighborhoods = <?= json_encode($result) ?>;
      var image = './icon/bicycle.png';
      var markers = [];
      var temp;
      var count=0;
      var max, maxindex;
      // var infowindow;

      function initMap() {
        map = new google.maps.Map(document.getElementById('map'), {
          center: {lat: 35.2321, lng: 129.083},
          zoom: 16
        });
        cal();
        drop();
      }

      function cal(){
        for (var i, i = 0; i < neighborhoods.length; i++) {
          for (var j, j = i+1; j < neighborhoods.length; j++) {
            if (neighborhoods[i].key == neighborhoods[j].key) {
              if ((Math.abs(Number(neighborhoods[i].lat) - Number(neighborhoods[j].lat))).toFixed(3) <= 0.002 && (Math.abs(Number(neighborhoods[i].lng) - Number(neighborhoods[j].lng))).toFixed(3) <= 0.002) {
                neighborhoods[i].count++;
                neighborhoods[j].key = i;
              }
            }
          }
        }
      }

      function drop() {
        max = neighborhoods[0].count;
        maxindex = 0;
        for (var i = 1; i < neighborhoods.length; i++) {
          if (max < neighborhoods[i]) {
            max = neighborhoods[i];
            maxindex = i;
          }
        }
        for (var i = 0; i < neighborhoods.length; i++) {
          if (isNaN(neighborhoods[i].key) == true) {
            addMarkerWithTimeout(neighborhoods[i], i * 200);
          }
        }
      }
  
      function click(count){
        markers[count].addListener('click', function() {
          if (neighborhoods[count].email == "") {
            neighborhoods[count].email = "익명";
          }
          document.all("content").innerHTML="사용자 : " + neighborhoods[count].email + "<br/>신고시간 " + neighborhoods[count].time + " " + neighborhoods[count].day + "<br/> [" + neighborhoods[count].key + "] " + neighborhoods[count].req + "<br/><br/>";
          for (var i = 1; i < neighborhoods.length; i++) {
            if (isNaN(neighborhoods[i].key) == false && neighborhoods[i].key == count) {
                if (neighborhoods[i].email == "") {
                  neighborhoods[i].email = "익명";
                }
                document.all("content").innerHTML += "사용자 : " + neighborhoods[i].email + "<br/>신고시간 " + neighborhoods[i].time + " " + neighborhoods[i].day + "<br/> [" + neighborhoods[count].key + "] " + neighborhoods[i].req + "<br/><br/>";
            }
          }
          $('#popup').bPopup({
            position: [$(document).width()-280,0]
          });
        });

      }

      function addMarkerWithTimeout(position, timeout) {
        window.setTimeout(function() {
          infowindow = new google.maps.InfoWindow({
            content: "hi",
            maxWidth: 200
           });
          if (position.key == "방지턱") {
             image = {
              url:'./icon/bump.png',
              scaledSize: new google.maps.Size(40,40)};
          }
          if (position.key == "가로등") {
             image = {
              url:'./icon/lamp.png',
              scaledSize: new google.maps.Size(40,40)};             
          }
          if (position.key == "도로") {
             image = {
              url:'./icon/road.png',
              scaledSize: new google.maps.Size(40,40)};            
          }
          if (position.key == "신호등") {
             image = {
              url:'./icon/trafficlight.png',
              scaledSize: new google.maps.Size(40,40)};            
          }
          if (position.key == "펜스") {
             image = {
              url:'./icon/fence.png',
              scaledSize: new google.maps.Size(40,40)};            
          }
          if (position.key == "낙석") {
             image = {
              url:'./icon/danger.png',
              scaledSize: new google.maps.Size(40,40)};            
          }
          if (position.key == "자전거도로") {
             image = {
              url:'./icon/bicycle.png',
              scaledSize: new google.maps.Size(40,40)};            
          }
          if (position.key == "교차로") {
             image = {
              url:'./icon/crossroad.png',
              scaledSize: new google.maps.Size(40,40)};           
          }
          var mar = new google.maps.Marker({
            position: {lat: Number(position.lat), lng: Number(position.lng)},
            map: map,
            animation: google.maps.Animation.DROP,
            icon: image
          });
          if (0.3 * (position.count+1)>=1) {
            mar.setOpacity(1);
          }
          else{
            mar.setOpacity(0.3 * (position.count+1));
          }
          markers.push(mar);
          click(count);
          count++;
          // markers[1].addListener('click', function() {
          //   infowindow.open(map, markers[1]);
          // });
          // console.log(position);
        }, timeout);
      }

    </script>
    <script async defer
      src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAQHVjnFyXNtUKc6ZgypcEOWyS_WxxhgKw&callback=initMap">
    </script>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="bpopup-master/jquery.bpopup.min.js"></script>