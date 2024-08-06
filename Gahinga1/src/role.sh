#check for role of given user
searchresult=$(awk "/$1/ && /admin/" user_sample.csv)
echo $searchresult
