const Razorpay = require('razorpay'); 
const { RAZORPAY_ID_KEY, RAZORPAY_SECRET_KEY } = process.env;
const mongoose = require('mongoose');
const mongoDB = "mongodb://localhost:27017/Orders";
const OrderModel = require('../Models/orders.js');
const razorpayInstance = new Razorpay({ key_id: RAZORPAY_ID_KEY, key_secret: RAZORPAY_SECRET_KEY});
let newOrder;
const renderProductPage = async (req, res) => {
    try {res.render('product');} 
    
    catch (error) { console.log(error.message);}
}

const createOrder = async (req, res) => {
    try {
        const amount = req.body.amount * 100;
        const options = {
            amount: amount,
            currency: 'INR',
            receipt: 'razorUser@gmail.com',
        }

        
  newOrder = OrderModel({
    userid: "7844541",
    name: "Laptop",
    amount: 50000,
    status: "Ordered"
});
        

        razorpayInstance.orders.create(options, (err, order) => {
            console.log(order,err);
            if (!err) {
                res.status(200).send({
                    success: true,
                    msg: 'Order Created',
                    order_id: order.id,
                    amount: amount,
                    key_id: RAZORPAY_ID_KEY,
                    product_name: req.body.name,
                    description: req.body.description,
                    contact: "8567345632",
                    name: "Salman Khan",
                    email: "abc@gmail.com",
                    
                });
                newOrder.paymentStatus = order.status;
                newOrder.razorpayOrderId = order.id;
                
            } else {
                newOrder.paymentStatus="Failure"
                    res.status(400).send({ success: false, msg: 'Something went wrong!' });
            }
            newOrder.save()
                .then((savedDocument) => {
                    console.log('Document Saved:', savedDocument);
                })
                .catch((error) => {
                    console.log("Error Saving Document", error);
                });
        });
    } catch (error) {console.log(error.message);}
}

mongoose.connect(mongoDB, {
    useNewUrlParser: true,
    useUnifiedTopology: true,
})
    .then(() => {
        console.log('Connected to the database');
    })
    .catch((error) => {
        console.log('Error connecting to the database', error);
    });

module.exports = {
    renderProductPage,
    createOrder
}


