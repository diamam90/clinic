db.createUser({
    user: "user1",
    pwd: "34525",
    roles: [
        {
            role: "readWrite",
            db: "clinic"
        }
    ]
});