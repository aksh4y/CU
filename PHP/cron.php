<?php
include_once ("simple_html_dom.php");
//use curl to get html content

  require_once 'db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
    
		
function getHTML($url,$timeout)
{
       $ch = curl_init($url); // initialize curl with given url
       curl_setopt($ch, CURLOPT_USERAGENT, $_SERVER["HTTP_USER_AGENT"]); // set  useragent
       curl_setopt($ch, CURLOPT_RETURNTRANSFER, true); // write the response to a variable
       //curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true); // follow redirects if any
       curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, $timeout); // max. seconds to execute
       curl_setopt($ch, CURLOPT_FAILONERROR, 1); // stop when it encounters an error
       return @curl_exec($ch);
}

$html=getHTML("http://www.christuniversity.in/ticker.php",5);

$result=mysql_query("Select Event from events limit 1") or die();
$row = mysql_fetch_object($result);
$event = $row->Event; 
$check=strcmp($html,$event);
if($check==0)
{
	echo "No New Events!";
}
else
{
	mysql_query("UPDATE events SET Event='$html'");			//Update Events
	echo "New Events!";
	include_once 'db_functions.php';
	include_once 'GCM.php';
    
		//$db = new DB_Functions();
        //$users = $db->getAllUsers();
        $users=mysql_query("Select * from gcm_users");
		if ($users != false)
            $no_of_users = mysql_num_rows($users);
        else
            $no_of_users = 0;
		if($no_of_users>0)
		{
			$gcm=new GCM();
			$message="You Have New Events!";
			//$message=array($message);
			while ($row = mysql_fetch_array($users)) 
			{
				$registration_ids=array($row["gcm_regid"]);
				$result=$gcm->send_notification($registration_ids,$message);
				echo $result;
			}
		}
		
			
			
			
}

?>