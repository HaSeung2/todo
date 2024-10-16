Use todo;
create table user(
    id bigint auto_increment primary key ,
    email varchar(250) not null  unique,
    password varchar(250) not null ,
    user_name varchar(250) not null ,
    role enum ('ADMIN','USER') not null ,
    created_at datetime(6),
    modified_at datetime(6)
);

create table todo(
    id bigint auto_increment primary key ,
    comment_count integer ,
    content varchar(250) not null ,
    title varchar(250) not null ,
    weather varchar(250) not null ,
    created_at datetime(6),
    modified_at datetime(6),
    user_id bigint,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

create table comment(
    id bigint auto_increment primary key ,
    comment_write_user_id bigint ,
    content varchar(250) not null ,
    user_name varchar(250) not null ,
    created_at datetime(6),
    modified_at datetime(6),
    todo_id bigint,
    FOREIGN KEY (todo_id) REFERENCES todo(id)
);

create table manager(
    id bigint auto_increment primary key ,
    manager_user_name varchar(250) not null ,
    todo_id bigint,
    user_id bigint,
    FOREIGN KEY (todo_id) REFERENCES todo(id),
    foreign key  (user_id) references  user(id)
);