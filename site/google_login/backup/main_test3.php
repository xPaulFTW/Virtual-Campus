<?php
// Report all PHP errors
error_reporting(E_ALL);
ini_set("display_errors", 1);


require_once '../google-api-php-client--PHP7.4/vendor/autoload.php';


//$client_id = '561737785048-1qb8k6gocp8q9ce21pcvrhbdtr20l3lf.apps.googleusercontent.com'; // 'CLIENT ID' on Google
//$service_account_name = 'adminphp@php-login-365307.iam.gserviceaccount.com'; // 'EMAIL ADDRESS' on Google

$key_file_location = '/var/php_keys/php-login-365307-69f13304118c.json';
$delegatedAdmin = 'admin@lmvineu.ro';

/**
 * Array of scopes you need for whatever actions you want to perform
 * See https://developers.google.com/admin-sdk/directory/v1/guides/authorizing
 * The admin.directory.user is needed to create the user, the admin.directory.group is needed to add the 
 * user to a group (see later on this file)
 */
$scopes = array(
		'https://www.googleapis.com/auth/admin.directory.user',
        'https://www.googleapis.com/auth/admin.directory.orgunit',
		'https://www.googleapis.com/auth/admin.directory.group'
);

$client = new Google_Client();
$client->setApplicationName("This is the name");
$client->setAuthConfig($key_file_location);
$client->setSubject($delegatedAdmin);
$client->setScopes($scopes);

$client->setAccessType('offline');
//$client->setPrompt('select_account consent');

$service_users = new Google\Service\Directory($client);

//$list = $service->users->listUsers(array('domain' => 'lmvineu.ro', 'maxResults' => 500));
//$list = $service->users->listUsers(array('domain' => 'lmvineu.ro', 'orderBy' => 'familyName' ,'maxResults' => 500));
//print_r($list);
//$org_unit="'/Elevi/Profesional/Clasa a IX-a DUAL'";

$org_unit="'/Elevi/Liceal/Clasa a XII-a A'";
$optParams = array(
    'customer' => 'my_customer',
    'query' => "orgUnitPath=".$org_unit,
    'maxResults' => 2,
    'orderBy' => 'familyName',
  );
  $list_users = $service_users->users->listUsers($optParams);
  //print_r($list_users);
if (count($list_users->getUsers()) == 0) {
    print "No users found.\n";
  } else {
    print "Users:\n";
    foreach ($list_users->getUsers() as $user) {
      //printf("%s (%s) %s\n", $user->getPrimaryEmail(), $user->getName()->getFullName(),$user->getorgUnitPath());
      printf("%s \n", $user->getlastLoginTime());
    }
}


$user_elev="test.pass@lmvineu.ro";
$user_resource = new Google\Service\Directory\User(
    array(
    'password' => 'Bac123q!',
    'changePasswordAtNextLogin' => 'true',
    )  
 );

  //$buff = $service_users->users->update($user_elev,$user_resource);
  //print_r($buff);



//$groups = $service->groups->listGroups(array('domain' => 'lmvineu.ro'));
//echo var_dump($groups);

/**
 * Create the user
 */

/*
$nameInstance = new Google_Service_Directory_UserName();
$nameInstance -> setGivenName('John');
$nameInstance -> setFamilyName('Doe');

$email = 'john.doe@domain.com';
$password = 'password';

$userInstance = new Google_Service_Directory_User();
$userInstance -> setName($nameInstance);
$userInstance -> setHashFunction("MD5");
$userInstance -> setPrimaryEmail($email);
$userInstance -> setPassword(hash("md5", $password));
try
{
	$createUserResult = $service->users->insert($userInstance);
	var_dump($createUserResult);
}
catch (Google_IO_Exception $gioe)
{
	echo "Error in connection: ".$gioe->getMessage();
}
catch (Google_Service_Exception $gse)
{
	echo "User already exists: ".$gse->getMessage();
}

/**
 * If you want it, add the user to a group
 */

 /*
$memberInstance = new Google_Service_Directory_Member();
$memberInstance->setEmail($email);
$memberInstance->setRole('MEMBER');
$memberInstance->setType('USER');
try
{
	$insertMembersResult = $service->members->insert('groupname@domain.com', $memberInstance);
}
catch (Google_IO_Exception $gioe)
{
	echo "Error in connection: ".$gioe->getMessage();
}

*/
?>