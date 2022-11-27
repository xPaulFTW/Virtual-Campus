<?php
error_reporting(E_ALL);
ini_set("display_errors", 1);

// include your composer dependencies
//require_once 'vendor/autoload.php';
require_once 'google-api-php-client--PHP7.4/vendor/autoload.php';

const GOOGLE_API_CLIENT_ID = '561737785048-1qb8k6gocp8q9ce21pcvrhbdtr20l3lf.apps.googleusercontent.com';
const GOOGLE_API_CLIENT_SECRET = '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';
const GOOGLE_API_ACCESS_TOKEN = 'AIzaSyC4eqmxwqhNmRoyamAczGTqE3y0B6pMgRU';

$client = new Google_Client();
$client->setClientId(GOOGLE_API_CLIENT_ID);
$client->setClientSecret(GOOGLE_API_CLIENT_SECRET);
//$client->setAccessToken(GOOGLE_API_ACCESS_TOKEN);
//$client->setUseObjects(true);

//$client = new Google\Client();
//$client->setApplicationName("Client_users");
//$client->setDeveloperKey("AIzaSyC4eqmxwqhNmRoyamAczGTqE3y0B6pMgRU");

$service = new Google\Service\Directory($client);

$groups = $service->groups->listGroups(array('domain' => 'lmvineu.ro'));
echo var_dump($groups);

/*
$query = 'Henry David Thoreau';
$optParams = [
  'filter' => 'free-ebooks',
];
$results = $service->volumes->listVolumes($query, $optParams);

foreach ($results->getItems() as $item) {
  echo $item['volumeInfo']['title'], "<br /> \n";
}


/*
$client = new Google\Client();
$client->setApplicationName("Client_Library_Examples");
$client->setDeveloperKey("AIzaSyC4eqmxwqhNmRoyamAczGTqE3y0B6pMgRU");

$service = new Google\Service\Books($client);
$query = 'Henry David Thoreau';
$optParams = [
  'filter' => 'free-ebooks',
];
$results = $service->volumes->listVolumes($query, $optParams);

foreach ($results->getItems() as $item) {
  echo $item['volumeInfo']['title'], "<br /> \n";
}
*/
/*
require DIR . 'lib/vendor/google/Google_Client.php';
require DIR . 'lib/vendor/google/contrib/Google_DirectoryService.php';

const GOOGLE_API_CLIENT_ID = '561737785048-1qb8k6gocp8q9ce21pcvrhbdtr20l3lf.apps.googleusercontent.com';
const GOOGLE_API_CLIENT_SECRET = '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';
const GOOGLE_API_ACCESS_TOKEN = 'AIzaSyC4eqmxwqhNmRoyamAczGTqE3y0B6pMgRU';

$client = new Google_Client();
$client->setClientId(GOOGLE_API_CLIENT_ID);
$client->setClientSecret(GOOGLE_API_CLIENT_SECRET);
$client->setAccessToken(GOOGLE_API_ACCESS_TOKEN);
//$client->setUseObjects(true);
$service = new Google_Directory($client);
$groups = $service->groups->listGroups(array('domain' => 'lmvineu.ro'));
echo var_dump($groups);
*/
?>