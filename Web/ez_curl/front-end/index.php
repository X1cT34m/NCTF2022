<?php
highlight_file(__FILE__);
$url = 'http://back-end:3000/flag?';
$input = file_get_contents('php://input');
$headers = (array)json_decode($input)->headers;
for($i = 0; $i < count($headers); $i++){
    $offset = stripos($headers[$i], ':');
    $key = substr($headers[$i], 0, $offset);
    $value = substr($headers[$i], $offset + 1);
    if(stripos($key, 'admin') > -1 && stripos($value, 'true') > -1){
        die('try hard');
    }
}
$params = (array)json_decode($input)->params;
$url .= http_build_query($params);
$url .= '&admin=false';
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
curl_setopt($ch, CURLOPT_TIMEOUT_MS, 5000);
curl_setopt($ch, CURLOPT_NOBODY, FALSE);
$result = curl_exec($ch);
curl_close($ch);
echo $result;
