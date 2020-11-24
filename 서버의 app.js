var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');

var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');

var app = express();
const mysql = require("mysql");

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/users', usersRouter);

// // catch 404 and forward to error handler
// app.use(function(req, res, next) {
//   next(createError(404));
// });

// error handler
// app.use(function(err, req, res, next) {
//   // set locals, only providing error in development
//   res.locals.message = err.message;
//   res.locals.error = req.app.get('env') === 'development' ? err : {};

//   // render the error page
//   res.status(err.status || 500);
//   res.render('error');
// });

module.exports = app;

var pool = mysql.createConnection({
  host:"localhost",
  user:"root",
  password:"yeonkyu",
  database:"blindCommunity"
})

app.get("/sign_up",(req,res) => {
  console.log("sign_up 도착");
  var inputData = req.query;
  console.log("들어온 데이터 : "+inputData.id);
  console.log("들어온 데이터 : "+inputData.pw);
  console.log("들어온 데이터 : "+inputData.nickname);
  
  pool.query("select * from UserTable where user_id = '"+inputData.id+"';", function(err,result,fields){
    if(err){
      throw err;
    }
    else{
      if(result[0]){ //아이디로 select했을때 하나라도 있는 경우
        console.log("아이디가 중복됩니다.");
        res.write("0",function(){ //response로 -1 줌
          res.end(); //response끝내기(return 과 같다고 보면 됨)
        });
      }
      else if(inputData.id==null || inputData.pw==null|| inputData.nickname==null){
        console.log("도착한 데이터가 이상합니다");
        res.write("-1",function(){ 
          res.end(); 
        });
      }
      else{
        //DB에 회원 정보 저장
        pool.query(
          `INSERT INTO UserTable(user_id, user_pw, nickname)\
           VALUES('${inputData.id}', '${inputData.pw}', '${inputData.nickname}');`,
          //"INSERT INTO UserTable(user_id, user_pw, nickname) VALUES ('" + inputData.id + "', " +
          //inputData.pw + "', '" + inputData.nickname + "');",
          function(err, result, fields) {
              if (err) {
                  throw err;
              } else {
                  console.log("새로운 회원 정보를 DB에 추가했습니다");
                  res.write("1", function() {
                      res.end();
                  });
              }
          }
        );
      }

    }

  });
});

app.get("/sign_in",(req,res) => {
  console.log("sign_in 도착");
  var inputData = req.query;
  console.log("들어온 데이터 : "+inputData.id);
  console.log("들어온 데이터 : "+inputData.pw);
  
  pool.query("select * from UserTable where user_id = '"+inputData.id+"';", function(err,result,fields){
    if(err){
      throw err;
    }
    else{
      if(result[0]){ //아이디로 select했을때 하나라도 있는 경우
        console.log("아이디를 찾았습니다.");
        
        pool.query(
          `SELECT * FROM UserTable\
           WHERE user_id = '${inputData.id}' and user_pw = '${inputData.pw}';`,
          //"INSERT INTO UserTable(user_id, user_pw, nickname) VALUES ('" + inputData.id + "', " +
          //inputData.pw + "', '" + inputData.nickname + "');",
          function(err, result2, fields) {
              if (err) {
                  throw err;
              } 
              else {
                if(result2[0]){
                  console.log("비밀번호가 일치합니다.");
                  res.write("1", function() {
                    res.end();
                  });
                 }
                else{
                  console.log("비밀번호가 일치하지 않습니다.");
                  res.write("-1", function() {
                    res.end();
                  });
                  }
                  
              }
          }
        );
      }
      else{
        console.log("아이디를 찾지 못했습니다.");
        res.write("0",function(){ //response로 0 줌
          res.end(); //response끝내기(return 과 같다고 보면 됨)
        });
      }

    }

  });
});

app.get("/search_free",(req,res) => {
  console.log("search_free 도착");
  var inputData = req.query;
  console.log("들어온 데이터 : "+inputData.cnt);
  
  pool.query(`select nickname,title,post_id from freePostTable f, UserTable u\
  where f.user_id = u.user_id order by post_id desc\
  limit ${inputData.cnt}, ${inputData.cnt+10};`,
   function(err,result,fields){
    if(err){
      throw err;
    }
    else{
      if(result[0]){ //아이디로 select했을때 하나라도 있는 경우
        console.log("free게시판 검색 성공");
        res.write(JSON.stringify(result),function(){
          res.end();
        })
      }
      else{
        console.log("free게시판에 글이 없습니다.");
        res.write("0",function(){ //response로 0 줌
          res.end(); //response끝내기(return 과 같다고 보면 됨)
        });
      }

    }

  });
});

app.get("/search_info",(req,res) => {
  console.log("search_free 도착");
  var inputData = req.query;
  console.log("들어온 데이터 : "+inputData.cnt);
  
  pool.query(`select nickname,title,post_id from infoPostTable f, UserTable u\
  where f.user_id = u.user_id order by post_id desc\
  limit ${inputData.cnt}, ${inputData.cnt+10};`,
   function(err,result,fields){
    if(err){
      throw err;
    }
    else{
      if(result[0]){ //아이디로 select했을때 하나라도 있는 경우
        console.log("info게시판 검색 성공");
        res.write(JSON.stringify(result),function(){
          res.end();
        })
      }
      else{
        console.log("info게시판에 글이 없습니다.");
        res.write("00",function(){ //response로 0 줌
          res.end(); //response끝내기(return 과 같다고 보면 됨)
        });
      }

    }

  });
});

app.get("/search_employee",(req,res) => {
  console.log("search_free 도착");
  var inputData = req.query;
  console.log("들어온 데이터 : "+inputData.cnt);
  
  pool.query(`select nickname,title,post_id from employPostTable f, UserTable u\
  where f.user_id = u.user_id order by post_id desc\
  limit ${inputData.cnt}, ${inputData.cnt+10};`,
   function(err,result,fields){
    if(err){
      throw err;
    }
    else{
      if(result[0]){ //아이디로 select했을때 하나라도 있는 경우
        console.log("employee게시판 검색 성공");
        res.write(JSON.stringify(result),function(){
          res.end();
        })
      }
      else{
        console.log("employee게시판에 글이 없습니다.");
        res.write("000",function(){ //response로 0 줌
          res.end(); //response끝내기(return 과 같다고 보면 됨)
        });
      }

    }

  });
});

app.get("/write_free",(req,res) => {
  console.log("write_free 도착");
  var inputData = req.query;
  console.log("들어온 데이터 : "+inputData.post_id);
  console.log("들어온 데이터 : "+inputData.title);
  console.log("들어온 데이터 : "+inputData.content);
  console.log("들어온 데이터 : "+inputData.user_id);

  pool.query(`insert into freePostTable(post_id, title, content, user_id, post_type)\
              values( '${inputData.post_id}', '${inputData.title}', '${inputData.content}', '${inputData.user_id}', '자유 게시판');`,
   function(err,result,fields){
    if(err){
      console.log("free게시판에 글쓰기 실패");
      res.write("0",function(){
        res.end();
      })
      throw err;
    }
    else{
      console.log("free게시판에 글쓰기 성공");
      res.write("1",function(){
        res.end();
      })
    }

  });
});

app.get("/write_info",(req,res) => {
  console.log("write_info 도착");
  var inputData = req.query;
  console.log("들어온 데이터 : "+inputData.post_id);
  console.log("들어온 데이터 : "+inputData.title);
  console.log("들어온 데이터 : "+inputData.content);
  console.log("들어온 데이터 : "+inputData.user_id);

  pool.query(`insert into infoPostTable(post_id, title, content, user_id, post_type)\
              values( '${inputData.post_id}', '${inputData.title}', '${inputData.content}', '${inputData.user_id}', '정보 게시판');`,
   function(err,result,fields){
    if(err){
      console.log("info게시판에 글쓰기 실패");
      res.write("0",function(){
        res.end();
      })
      throw err;
    }
    else{
      console.log("info게시판에 글쓰기 성공");
      res.write("1",function(){
        res.end();
      })
    }

  });
});

app.get("/write_employ",(req,res) => {
  console.log("write_employ 도착");
  var inputData = req.query;
  console.log("들어온 데이터 : "+inputData.post_id);
  console.log("들어온 데이터 : "+inputData.title);
  console.log("들어온 데이터 : "+inputData.content);
  console.log("들어온 데이터 : "+inputData.user_id);

  pool.query(`insert into employPostTable(post_id, title, content, user_id, post_type)\
              values( '${inputData.post_id}', '${inputData.title}', '${inputData.content}', '${inputData.user_id}', '취업 게시판');`,
   function(err,result,fields){
    if(err){
      console.log("employ게시판에 글쓰기 실패");
      res.write("0",function(){
        res.end();
      })
      throw err;
    }
    else{
      console.log("employ게시판에 글쓰기 성공");
      res.write("1",function(){
        res.end();
      })
    }

  });
});

app.get("/search_free_content",(req,res) => {
  console.log("search_free_content 도착");
  var inputData = req.query;
  console.log("들어온 데이터 : "+inputData.post_id);

  pool.query(`select nickname, title, content from UserTable u, freePostTable f\
              where u.user_id = f.user_id and f.post_id = '${inputData.post_id}';`,
   function(err,result,fields){
    if(err){
      throw err;
    }
    else{
      if(result[0]){ //아이디로 select했을때 하나라도 있는 경우
        console.log("free게시판 content검색 성공");
        res.write(JSON.stringify(result),function(){
          res.end();
        })
      }
      else{
        console.log("free게시판에서 해당 post_id를 찾지 못했습니다.");
        res.write("-1",function(){
          res.end(); 
        });
      }
    }

  });
});

app.get("/search_info_content",(req,res) => {
  console.log("search_info_content 도착");
  var inputData = req.query;
  console.log("들어온 데이터 : "+inputData.post_id);

  pool.query(`select nickname, title, content from UserTable u, infoPostTable f\
              where u.user_id = f.user_id and f.post_id = '${inputData.post_id}';`,
   function(err,result,fields){
    if(err){
      throw err;
    }
    else{
      if(result[0]){ //아이디로 select했을때 하나라도 있는 경우
        console.log("info게시판 content검색 성공");
        res.write(JSON.stringify(result),function(){
          res.end();
        })
      }
      else{
        console.log("info게시판에서 해당 post_id를 찾지 못했습니다.");
        res.write("-1",function(){
          res.end(); 
        });
      }
    }

  });
});

app.get("/search_employ_content",(req,res) => {
  console.log("search_employ_content 도착");
  var inputData = req.query;
  console.log("들어온 데이터 : "+inputData.post_id);

  pool.query(`select nickname, title, content from UserTable u, employPostTable f\
              where u.user_id = f.user_id and f.post_id = '${inputData.post_id}';`,
   function(err,result,fields){
    if(err){
      throw err;
    }
    else{
      if(result[0]){ //아이디로 select했을때 하나라도 있는 경우
        console.log("employ게시판 content검색 성공");
        res.write(JSON.stringify(result),function(){
          res.end();
        })
      }
      else{
        console.log("employ게시판에서 해당 post_id를 찾지 못했습니다.");
        res.write("-1",function(){
          res.end(); 
        });
      }
    }

  });
});

app.get("/write_comment",(req,res) => {
  console.log("write_comment 도착");
  var inputData = req.query;
  console.log("들어온 데이터 : "+inputData.post_id);
  console.log("들어온 데이터 : "+inputData.user_id);
  console.log("들어온 데이터 : "+inputData.comment_content);
  console.log("들어온 데이터 : "+inputData.comment_id);

  pool.query(`insert into commentTable(post_id,user_id,comment_content,comment_id)\
              values('${inputData.post_id}','${inputData.user_id}','${inputData.comment_content}','${inputData.comment_id}');`,
   function(err,result,fields){
    if(err){
      console.log("댓글 쓰기 에러");
      res.write("-1",function(){
        res.end();
      })
      throw err;
    }
    else{
      console.log("댓글을 성공적으로 추가하였습니다.");
      res.write("1",function(){
        res.end(); 
      });
      
    }

  });
});

app.get("/search_comment",(req,res) => {
  console.log("search_comment 도착");
  var inputData = req.query;
  console.log("들어온 데이터 : "+inputData.post_id);

  pool.query(`select nickname, comment_content, u.user_id\
              from UserTable u, commentTable c\
              where u.user_id = c.user_id and c.post_id = '${inputData.post_id}'\
              order by c.comment_id asc;`,
   function(err,result,fields){
    if(err){
      console.log("댓글 찾기 에러");
      res.write("-1",function(){
        res.end();
      })
      throw err;
    }
    else{
      if(result[0]){ //아이디로 select했을때 하나라도 있는 경우
        console.log("해당 게시판에서 댓글 찾기 성공");
        res.write(JSON.stringify(result),function(){
          res.end();
        })
      }
      else{
        console.log("해당 게시판에 댓글이 없습니다.");
        res.write("0",function(){
          res.end(); 
        });
      }
    }

  });
});

app.get("/find_trace",(req,res) => {
  console.log("find_trace 도착");
  var inputData = req.query;
  console.log("들어온 데이터 : "+inputData.user_id);

  pool.query(`(select title,post_id,post_type from freePostTable where user_id = '${inputData.user_id}')\
              UNION (select title,post_id,post_type from infoPostTable where user_id = '${inputData.user_id}')\
              UNION (select title,post_id,post_type from employPostTable where user_id = '${inputData.user_id}')\
              order by post_id desc`,
   function(err,result,fields){
    if(err){
      console.log("게시글 찾기 에러");
      res.write("-1",function(){
        res.end();
      })
      throw err;
    }
    else{
      if(result[0]){ //아이디로 select했을때 하나라도 있는 경우
        console.log("작성한 게시글 찾기 성공");
        res.write(JSON.stringify(result),function(){
          res.end();
        })
      }
      else{
        console.log("작성한 게시글이 없습니다.");
        res.write("0",function(){
          res.end(); 
        });
      }
    }

  });
});

app.get("/check_writerOrNot",(req,res) => {
  console.log("check_writerOrNot 도착");
  var inputData = req.query;
  console.log("들어온 데이터 : "+inputData.user_id);
  console.log("들어온 데이터 : "+inputData.post_id);

  pool.query(`(select * from freePostTable where post_id = '${inputData.post_id}' and user_id = '${inputData.user_id}')\
              UNION (select * from infoPostTable where post_id = '${inputData.post_id}' and user_id = '${inputData.user_id}')\
              UNION (select * from employPostTable where post_id = '${inputData.post_id}' and user_id = '${inputData.user_id}')`,
   function(err,result,fields){
    if(err){
      console.log("check_writerOrNot 찾기 에러");
      res.write("-1",function(){
        res.end();
      })
      throw err;
    }
    else{
      if(result[0]){ //아이디로 select했을때 하나라도 있는 경우
        console.log("글쓴이가 맞습니다.");
        res.write("1",function(){
          res.end();
        })
      }
      else{
        console.log("글쓴이가 아닙니다.");
        res.write("0",function(){
          res.end(); 
        });
      }
    }

  });
});


console.log("서버 시작");