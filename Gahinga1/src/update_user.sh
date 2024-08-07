#update record in users file
updateuser=$(sed -i '$1s/.*/$2    $3   $4    $5    $6    $7   $8    $9    ${10}    ${11}    ${12}    ${13}/' user-store.txt)
echo $?

