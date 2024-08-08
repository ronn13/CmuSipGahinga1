#update record in users file
# updateuser=$(sed -i '${1}s/.*/$2    $3   $4    $5    $6    $7   $8    $9    ${10}    ${11}    ${12}    ${13}/' user-store.txt)
# echo $?

# Construct the sed command
# sed_command=s/^$1 .*/$1    $2   $3    $4    $5    $6   $7    $8    $9    ${10}    ${11}    ${12}/

# # Execute the sed command
#updateuser=$(sed -i "s/^$1 .*/$1    $2   $3    $4    $5    $6   $7    $8    $9    ${10}    ${11}    ${12}/" user-store.txt)
updateuser=$(sed -i "s~^$1 .*~$1    $2   $3    $4    $5    $6   $7    $8    $9    ${10}    ${11}    ${12}~" user-store.txt)
echo $?