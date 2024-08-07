#check for role of given user
searchresult=$(awk "/$1/ && /admin/" user-store.txt)
echo $searchresult
