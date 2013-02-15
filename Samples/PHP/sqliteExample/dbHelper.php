<?php
	class SiteData {
	
		const databaseName = 'sqlite:data/db.sqlite';
		
		const targetFile = 'data/db.sqlite';
		
		const serviceTable = 'servicedata_1.0';
		const countriesTable = 'country_1.0';
		const currencyTable = 'currency_1.0';
		const propositionsTableName = 'servicepropositiondata_1.0';
		const territoryTable = 'serviceterritorydata_1.0';
		const userTable = 'users_1.0';
		
		const appKey = '123acme';
		const hostName = 'www.datownia.com';
		const clientId = '123acme';
		const clientSecret = 'hohohomerryxmas';
		const publisher = 'acme';
		
		/*
		To handle database version, should send timestamp as param in call.
		If db not updated since timestamp, no db returned.
		*/
		function getDatabase() {
			$requestScope = "Read|".self::publisher."|*";
			$requestUrl = "https://".self::hostName."/api/app/".self::publisher."/".self::appKey;
			
			//GET AN ACCESS TOKEN
			$url = "https://".self::hostName."/oauth2/token";
			$fields = array(
				"client_id" => self::clientId,
				"client_secret" => self::clientSecret,
				"redirect_uri" => "http://musically.nfshost.com/site/index.html",
				"grant_type" => "client_credentials",
				"scope" => $requestScope
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
			
			//echo $AccessToken;
			
			//NOW YOU HAVE THE TOKEN SIGN THE REQUEST AND GET THE DATA
			//1. create new request
			//2. sign it
			$requestFields = array(
				"client_id" => self::clientId,
				"client_secret" => self::clientSecret,
				"grant_type" => "client_credentials",
				"scope" => $requestScope,
				"Authorization" => "Bearer ".$AccessToken
			);
			foreach($requestFields as $key=>$value) { $reqFields_string .= $key.'='.$value.'&'; }
			rtrim($reqFields_string,'&');
			$ch2 = curl_init($requestUrl);	
			curl_setopt($ch2,CURLOPT_URL, $requestUrl);	
			//Because we'll be writing to a file you no longer specify the CURLOPT_RETURNTRANSFER option.
			//curl_setopt($ch2, CURLOPT_RETURNTRANSFER, 0);
			//mitigate against ssl cert errors
			curl_setopt($ch2, CURLOPT_SSL_VERIFYPEER, false);
			$sign = array(
				'client_id: '.self::clientId,
				'Authorization: Bearer '.$AccessToken
			);
			curl_setopt($ch2, CURLOPT_HTTPHEADER, $sign);
			$fp = fopen(self::targetFile, 'w');
			curl_setopt($ch2, CURLOPT_FILE, $fp);
			$finalResult = curl_exec($ch2);
			if ( !$finalResult || curl_getinfo( $ch2, CURLINFO_HTTP_CODE ) != 200 ) {
				die( "There was an error getting the SQL database, aborting." );
			}
			curl_close($ch2);
			fclose($fp);
		}

		function listTables(){
			$db = new PDO(self::databaseName);
			$result = $db->query("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name;");
			foreach ($result as $entry) {
				echo 'Name: ' . $entry['name'];
				echo "<br />";
			}
		}
		
		function stripPipes($originalString){
			$pieces = explode("|", $originalString);
			$result = "";
			foreach($pieces as &$piece){
				//echo $piece;
				if (!empty($result)){
					$result = $result.",";
				}
				$result = $result."'".$piece."'";
			}
			return $result;
		}
		
		function toStringArray($pipeDelimString){
			$pieces = explode("|", $pipeDelimString);
			$result = array();
			foreach($pieces as &$piece){
				if (strlen($piece) > 0)
				array_push($result, $piece);
			}
			return $result;
		}
		
		//�	Service Provider name
		function GetServiceProviders(){
			$serviceProviders = array();			
			$db = new PDO(self::databaseName);
			$result = $db->query("SELECT DISTINCT Service FROM [".self::serviceTable."] ORDER BY Service ASC;");
			foreach ($result as $entry) {
				array_push($serviceProviders, $entry['Service']);
			}
			return $serviceProviders;
		}
		
		function login($myusername, $mypassword) {
			$db = new PDO(self::databaseName);
			$query = "SELECT count(*) FROM [".self::userTable."] WHERE username='".$myusername."' AND password ='".$mypassword."';";
			//echo $query;
			$result = $db->query($query);
			$counter = 0;
			foreach ($result as $entry) {
				foreach ($entry as $val) {
					if ($val > 0)
						$counter += $counter + 1;
				}
			}
			return ($counter > 0);
		}
				
		function mainQueryResults($providers, $types, $terms, $countries, $regions, $propositions) {
			$queryString = "select distinct svc.service, svc.tier, svc.type, svc.terms, svc.[global users], svc.[facebook users], svc.[tier users],
				cunt.Country, terr.[cost - min], terr.[cost - max], terr.limits, terr.[distribution partner], terr.[fulfilment partner], curr.Symbol";
				//iterate propositions and select
			if (count($propositions) == 0){
				$propositions = self::GetPropositions();
			}
			foreach ($propositions as $prop) {
				if ($prop != 'all')
				$queryString = $queryString.', prop.['.$prop.']';
			}
			$queryString = $queryString." from [".self::serviceTable."] svc
				left join [".self::territoryTable."] terr on terr.service = svc.service and terr.tier = svc.tier
				left join [".self::countriesTable."] cunt on cunt.Country_Code = terr.Territory
				left join [".self::currencyTable."] curr on curr.[Currency Code] = cunt.[Country Currency]
				left join [".self::propositionsTableName."] prop on svc.Service = prop.Service and svc.Tier = prop.Tier
				WHERE svc.service <> ''";
			
			if ($providers != "''")
			{
				$queryString = $queryString." AND svc.service in (".$providers.")";
			}
			
			if ($types != "''")
			{
				$queryString = $queryString." AND svc.type in (".$types.")";
			}
			if ($terms != "''")
			{
				$queryString = $queryString." AND svc.terms in (".$terms.")";
			}
			
			//countries take priority over region
			if ($countries != "''")
			{
				$queryString = $queryString." AND cunt.Country in (".$countries.")";
			}
			else
			{
				if ($regions != "''")
				{
					$queryString = $queryString." AND cunt.Region in (".$regions.")";
				}
			}
			
			$db = new PDO(self::databaseName);
			//echo $queryString;
			$result = $db->query($queryString);
			return $result;
		}
	}
?>