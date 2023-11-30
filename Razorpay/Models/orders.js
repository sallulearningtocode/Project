const mongoose = require('mongoose');
const OrderSchema = new mongoose.Schema({
    userid:{
        type:String,
        required:true
    },
    name:{
        type:String,
    },
    razorpayOrderId:{
        type:String,
        required:true
    },
    amount:{
        type:Number,
        required:true
    },
    status:{
        type:String,
        required:true
    },
    paymentStatus:{
        type:String,
    },
    signature:{

    }
})

const OrderModel = mongoose.model('OrderModel',OrderSchema)
module.exports=OrderModel;