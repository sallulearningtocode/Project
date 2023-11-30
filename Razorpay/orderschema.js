const mongoose = require('mongoose')

const order = {
    "name":"abc",
    "measurement":"",
    "price":"",
}

const orderSchema = new mongoose.Schema(
    {
      userid: {
        type: String,
        required: true,
      },
      productId: {
        type: String,
        required: true,
      },
      razorpayOrderId:{

      },
      quantity:{

      },
      status:{

      },
      amount:{

      },
      paymentStatus:{

      }
      name: String,
      email: {
        type: String,
        required: true,
        unique: true,
      },
      address: String,
      phone: String,
      profilepic: String,
      resetPasswordOtpSecret: String,
      resetPasswordOtp: String,
    },
    {
      timestamps: true, // This option adds createdAt and updatedAt f