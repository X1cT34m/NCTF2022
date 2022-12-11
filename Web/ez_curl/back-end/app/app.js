const express = require('express');

const app = express();

const port = 3000;
const flag = process.env.flag;

app.get('/flag', (req, res) => {
    if(!req.query.admin.includes('false') && req.headers.admin.includes('true')){
        res.send(flag);
    }else{
        res.send('try hard');
    }
});

app.listen({ port: port , host: '0.0.0.0'});
