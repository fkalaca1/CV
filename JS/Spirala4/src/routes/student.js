const express = require('express');
const StudentControllers = require('../controllers/student');
const router = express.Router();

router.route('/')
    .get(StudentControllers.getAllStudents);

module.exports = router;
