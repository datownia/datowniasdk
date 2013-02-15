<?php
	//phpinfo();
	//get the parameter from URL
	$docName = $_GET['docName'];
	$hostName = 'www.datownia.com';
	$clientId = '<your client id>';
	$clientSecret = '<your client secret>';
	$publisher = '<api publisher>';
	
	
	//GET AN ACCESS TOKEN
	$url = "https://".$hostName."/oauth2/token";
	$fields = array(
        "client_id" => $clientId,
        "client_secret" => $clientSecret,
        "redirect_uri" => "",
		"grant_type" => "client_credentials",
		"scope" => "Read|".$publisher."|/".$docName
    );
    foreach($fields as $key=>$value) { $fields_string .= $key.'='.$value.'&'; }
	rtrim($fields_string,'&');
	if (! function_exists('curl_init')) {
	  echo 'requires the CURL PHP extension';
	}
	$ch = curl_init($url);
	curl_setopt($ch,CURLOPT_URL, $url);
	curl_setopt($ch,CURLOPT_POST,count($fields));
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
	curl_setopt($ch,CURLOPT_POSTFIELDS,$fields_string);
	//mitigate against ssl cert errors
	curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
	$result = curl_exec($ch);
	$responseObj = json_decode($result);
	$AccessToken = $responseObj->access_token;
	curl_close($ch);
	
	//NOW YOU HAVE THE TOKEN SIGN THE REQUEST AND GET THE DATA
	//1. create new request
	//2. sign it
	$requestUrl = "https://".$hostName."/api/doc/".$publisher."/vmax/".$docName;
	$ch2 = curl_init($requestUrl);	
	curl_setopt($ch2,CURLOPT_URL, $requestUrl);	
	curl_setopt($ch2, CURLOPT_RETURNTRANSFER, 1);	
	//mitigate against ssl cert errors
	curl_setopt($ch2, CURLOPT_SSL_VERIFYPEER, false);
	$sign = array(
        'client_id: '.$clientId,
		'Authorization: Bearer '.$AccessToken
    );
	curl_setopt($ch2, CURLOPT_HTTPHEADER, $sign);
	$finalResult = curl_exec($ch2);
	if ($finalResult === FALSE) {
		//echo curl_error($ch2);
	}
	curl_close($ch2);
	
	// PARSE THE DATA INTO A TABLE
	$value = $finalResult;
	$json = json_decode($value);
	$jsonContent = $json->{'contents'}; // this should be the array of rows
	if ($jsonContent != null) {
		echo '<table id="publishedTable" class="tableSorter tigerStripe" style="float:left;table-layout:fixed; font-size:0.8em;">';
		foreach ($jsonContent as $i => $values) {
			//create headers
			if ($i == 0){
				echo '<thead>';
				foreach ($values as $key => $value) {
					if ($key != 'row_num' && $key != 'checksum')
					print '<th class="nameH" style="width:auto;">'.$key.'</th>';
				}
				echo '</thead>';
				echo '<body>';
			}							
			//create rows
			echo '<tr>';
			foreach ($values as $key => $value) {
				if ($key != 'row_num' && $key != 'checksum'){
					//if value starts with http make link clickable
					$isLink = false;
					if (!empty($value))
						if (strlen($value) > 5)
							if (substr($value, 0, 4) == "http")
								$isLink = true;
					if ($isLink)
						print '<td><a href="'.$value.'">'.$value.'</a></td>';
					else
						print '<td>'.$value.'</td>';
				}
			}
			echo '</tr>';
			if ($i == count){
				echo '</tbody>';
			}
		}
		echo '</table>';
	}
	else {
		echo 'Data not published';
	}
?>