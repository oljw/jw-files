'use strict';
var mongoose = require('mongoose');
var Schema = mongoose.Schema;


// var TaskSchema = new Schema({
//   name: {
//     type: String,
//     required: 'Kindly enter the name of the task'
//   },
//   Created_date: {
//     type: Date,
//     default: Date.now
//   },
//   status: {
//     type: [{
//       type: String,
//       enum: ['pending', 'ongoing', 'completed']
//     }],
//     default: ['pending']
//   }
// });

// module.exports = mongoose.model('Tasks', TaskSchema);

var videoSchema = new Schema({
  _id:  {
    type: Number, default: 0
  },
  path: {
    type: String,
    required: 'Kindly enter the path'
  }
});

module.exports = mongoose.model('Tasks', videoSchema);