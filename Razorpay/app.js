require("dotenv").config();
const { error } = require("console");
const OrderModel = require('./Models/orders.js');
const crypto = require('crypto')
const app = require('express')();
const {RAZORPAY_ID_KEY,RAZORPAY_SECRET_KEY}=process.env;
const http = require('http').Server(app);

const paymentRoute = require('./routes/paymentRoute');

app.use('/',paymentRoute)

app.post("/updatePaymentStatus",async (req,res)=>{
  console.log("\n\nentered update payment route");
        const { razorpay_order_id, signature, razorpay_payment_id} = req.body;
        
        const generated_signature = crypto
        .createHmac('sha256',RAZORPAY_SECRET_KEY)
        .update(razorpay_order_id+"|"+razorpay_payment_id)
        .digest('hex');
        
        // console.log(generated_signature,"\n\n");
        // console.log("\nSignature\n",signature);
        
        if (generated_signature === signature) {
          console.log("Signatures are Same");
          OrderModel.findOneAndUpdate(
            { razorpayOrderId: razorpay_order_id }, { paymentStatus:"Success"}, { new: true, upsert: false}
            ).then(updatedOrder => {
              
              if(!updatedOrder) {console.log('Not Updated');}
              
              else {console.log('Updated');}  
            })
            .catch(err=>{
                console.log('Error',err);
            }) 
      }

      else if(OrderModel.findOneAndUpdate ({razorpayOrderId:razorpay_order_id,paymentStatus:"Created"}))
  {
    OrderModel.findOneAndUpdate(
    { razorpayOrderId: razorpay_order_id }, { paymentStatus:"Failed"}, { new: true, upsert: false}
      ).then(updatedOrder => {
      
          if(!updatedOrder) {console.log('Not Updated');}
      
              else {console.log('Updated');}  
        })
        .catch(err=>{
              console.log('Error',err);
      })
}
      else{
        if(OrderModel.find({razorpayOrderId:razorpay_order_id,paymentStatus:"Success"})) 
        return 

        else
        {
          OrderModel.findOneAndUpdate(
          { razorpayOrderId: razorpay_order_id }, { paymentStatus:"Failed"}, { new: true, upsert: false}
          ).then(updatedOrder => {
            
            if(!updatedOrder) {console.log('Not Updated');}
            
            else {console.log('Updated');}  
          })
          .catch(err=>{
              console.log('Error',err);
          })
        }
      }
}
);

// Routes
//CreateOrder
//Initiate_Payment (req.order_id)
//updatePayment 

app.post("/retryPayment",async (req,res)=>{

  // console.log("Entered Retry");
  // const { razorpay_order_id,paymentStatus,signature} = req.body;
  // console.log(razorpay_order_id);
  // console.log(paymentStatus);
  // console.log(signature);
  // const query = OrderModel.find({razorpayOrderId:razorpay_order_id,paymentStatus:"Failed"}); 
  // if(await query.exec()>0) 
  // {   
  //        console.log("enterd if");  
  //         return 
  // }
  //       else
  //       {
  //         console.log("entered else");
  //         OrderModel.findOneAndUpdate(
  //         { razorpayOrderId: razorpay_order_id }, { paymentStatus:"failed"}, { new: true, upsert: false}
  //         ).then(updatedOrder => {
            
  //           if(!updatedOrder) {console.log('Not Updated');}
            
  //           else {console.log('Updated');}  
  //         })
  //         .catch(err=>{
  //             console.log('Error',err);
  //         })
  //       }

})

http.listen(4000, function(){
    console.log('Server is running');
});



