<?php
echo 'getting database...';
include ('dbHelper.php');
$dataHelper = new SiteData;
$dataHelper->getDatabase();
echo 'got new database!';
?>