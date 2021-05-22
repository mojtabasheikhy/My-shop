package com.example.myshop.FireStore

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.myshop.R
import com.example.myshop.Utils.ConstVal
import com.example.myshop.View.activity.*
import com.example.myshop.View.fragment.Dashbord
import com.example.myshop.View.fragment.Order
import com.example.myshop.View.fragment.ProductFragment
import com.example.myshop.View.fragment.sold
import com.example.myshop.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage

class FireStore {

    var myFirestore = FirebaseFirestore.getInstance()

    fun RegisterUserToFireStore(activity: Register, userinfo: user) {
        myFirestore.collection(ConstVal.Collection_Users)
            .document(userinfo.user_id)
            .set(userinfo, SetOptions.merge())
            .addOnCompleteListener {
                it.addOnSuccessListener {
                    activity.RegisterSuccess()
                }
            }
            .addOnFailureListener {
                activity.RegisterFailed()
            }
    }

    fun GetCurrentUserID(): String {
        val CurrentUser = FirebaseAuth.getInstance().currentUser
        var CurrentUserID = ""
        if (CurrentUser != null) {
            CurrentUserID = CurrentUser.uid
        }
        return CurrentUserID
    }

    fun GetUserDetailFromFireStore(activity: Activity) {
        myFirestore.collection(ConstVal.Collection_Users)
            .document(GetCurrentUserID())
            .get()
            .addOnCompleteListener {
                it.addOnSuccessListener {
                    val user = it.toObject(user::class.java)
                    when (activity) {
                        is Login -> {
                            if (user != null) {
                                activity.UserLoginSuccess(user)
                            }
                        }
                        is Settings -> {
                            if (user != null) {
                                activity.GetUserDetailSettingsSuccess(user)
                            }
                        }
                    }
                }
            }
            .addOnFailureListener {
                when (activity) {
                    is Login -> {
                        activity.HideDialog()
                        activity.ShowSnackbar(it.message.toString(), false)
                    }
                    is Settings -> {
                        activity.HideDialog()
                        activity.ShowSnackbar(it.message.toString(), false)
                    }
                }
            }
    }

    fun Update_User_Detail(activity: Activity, UserHashMap: HashMap<String, Any>) {
        myFirestore.collection(ConstVal.Collection_Users)
            .document(GetCurrentUserID())
            .update(UserHashMap)
            .addOnCompleteListener {
                it.addOnSuccessListener {
                    when (activity) {
                        is CompleteProfile -> {
                            activity.UpdateUserDetailSuccessfully()
                        }
                    }
                }
            }
            .addOnFailureListener {
                when (activity) {
                    is CompleteProfile -> {
                        activity.HideDialog()
                        activity.ShowSnackbar(
                            activity.resources.getString(R.string.errorWhenUpadteData),
                            false
                        )
                    }
                }
            }
    }

    //TODO Call when change profile in edit actvity
    fun UpdateProductImageProfileWhenchange(userid: String, hashMap: HashMap<String, Any>) {
        myFirestore.collection(ConstVal.Collection_product).document(ConstVal.user_id)
            .update(hashMap)
    }

    fun UploadImageToCloudStore(activity: Activity, imageExtension: Uri, ImageType: String) {

        val sref = FirebaseStorage.getInstance().reference.child(
            ImageType + System.currentTimeMillis() + "." + ConstVal.GetFileExtention(
                activity,
                imageExtension
            )
        )
        sref.putFile(imageExtension)

            .addOnSuccessListener {
                it.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                    when (activity) {
                        is CompleteProfile -> {
                            /**گرفتن ادرس عکس اپلود شده و ارسال دوباره ان به دیتا بیس*/
                            activity.uploadImageSuccess(uri)

                        }
                        is AddProduct -> {
                            activity.UploadImageSuccess(uri)
                        }
                    }
                }
            }
            .addOnFailureListener {
                when (activity) {
                    is CompleteProfile -> {
                        activity.HideDialog()
                    }
                    is AddProduct -> {
                        activity.HideDialog()
                    }
                }
            }
    }
    fun UploadvideoToCloudStore(activity: Activity, Videouri: Uri, videoType: String) {

        val sref = FirebaseStorage.getInstance().reference.child(videoType + System.currentTimeMillis() + "." + ConstVal.GetFileExtention(activity, Videouri))
        sref.putFile(Videouri)

            .addOnSuccessListener {
                it.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                    when (activity) {
                        is AddProduct -> {
                            activity.UploadVideoSuccess(uri)
                        }
                    }
                }
            }
            .addOnFailureListener {
                when (activity) {

                    is AddProduct -> {
                        activity.HideDialog()
                    }
                }
            }
    }

    fun addproductToFireStore(activity: AddProduct, productDataClass: ProductDataClass) {
        myFirestore.collection(ConstVal.Collection_product)
            .document()
            .set(productDataClass, SetOptions.merge())
            .addOnCompleteListener {
                it.addOnSuccessListener {
                    activity.successAddproduct()
                }
            }
            .addOnFailureListener {
                activity.failedAddproduct()
            }
    }

    fun GetMyProduct(fragment: Fragment) {
        myFirestore.collection(ConstVal.Collection_product)
            .whereEqualTo(ConstVal.user_id_seller, GetCurrentUserID())
            .get()
            .addOnCompleteListener {
                it.addOnSuccessListener { Products ->
                    val MyProductList: ArrayList<ProductDataClass> = ArrayList()
                    for (i in Products.documents) {
                        val Myproduct = i.toObject(ProductDataClass::class.java)
                        Myproduct?.product_id = i.id
                        MyProductList.add(Myproduct!!)
                    }
                    when (fragment) {
                        is ProductFragment -> {
                            fragment.successGetMyProductFromFireStore(MyProductList)
                        }
                    }
                }
            }
            .addOnFailureListener {
                when (fragment) {
                    is ProductFragment -> {
                        fragment.failedgetproduct()
                    }
                }
            }
    }

    fun GetAllProduct(fragment: Fragment) {
        myFirestore.collection(ConstVal.Collection_product)
            .get()
            .addOnCompleteListener {
                it.addOnSuccessListener { Products ->
                    val allProductList: ArrayList<ProductDataClass> = ArrayList()
                    for (i in Products.documents) {
                        val Allproduct = i.toObject(ProductDataClass::class.java)
                        Allproduct?.product_id = i.id
                        allProductList.add(Allproduct!!)
                    }
                    when (fragment) {
                        is Dashbord -> {
                            fragment.successGetAllProductFromFireStore(allProductList)
                        }
                    }

                }
            }
            .addOnFailureListener {
                //TODO show snackbar failed
            }
    }

    fun deleteMyProduct(fragment: ProductFragment, productId: String) {
        myFirestore.collection(ConstVal.Collection_product)
            .document(productId)
            .delete()
            .addOnSuccessListener {
                fragment.SuccessDeleteMyProduct()
            }
            .addOnFailureListener {
                fragment.failedDeleteMyProduct()
            }
    }

    fun GetDetailProduct(activity: DetailProduct, productId: String) {
        myFirestore.collection(ConstVal.Collection_product)
            .document(productId)
            .get()
            .addOnSuccessListener {
                val product = it.toObject(ProductDataClass::class.java)
                activity.successGetDetailProduct(product!!)
            }
            .addOnFailureListener {
                activity.FailedGetDetailProduct()
            }
    }

    fun CreateCartItem(activity: DetailProduct, cartDataclass: CartDataClass) {
        myFirestore.collection(ConstVal.collection_cart)
            .document()
            .set(cartDataclass, SetOptions.merge())
            .addOnSuccessListener {
                activity.AddCartSuccess()
            }
            .addOnFailureListener {
                activity.AddCartfailed()

            }
    }

    fun CheckProductExistInCart(activity: DetailProduct, productId: String) {
        myFirestore.collection(ConstVal.collection_cart)
            .whereEqualTo(ConstVal.user_id_buyer, GetCurrentUserID())
            .whereEqualTo(ConstVal.product_id, productId)
            .get()
            .addOnSuccessListener {
                if (it.documents.size > 0) {
                    activity.successExistInCart()
                } else
                    activity.HideDialog()
            }
            .addOnFailureListener {
                activity.HideDialog()
            }
    }

    fun GetCart(activity: Activity) {
        myFirestore.collection(ConstVal.collection_cart)
            .whereEqualTo(ConstVal.user_id_buyer, GetCurrentUserID())
            .get()
            .addOnSuccessListener {
                val cartitemList: ArrayList<CartDataClass> = ArrayList()
                for (i in it.documents) {
                    val cartitem = i.toObject(CartDataClass::class.java)!!
                    cartitem.cart_id = i.id
                    cartitemList.add(cartitem)
                }
                when (activity) {
                    is cartlist -> {
                        activity.GetCartListSuccess(cartitemList)
                    }
                    is Checkout -> {
                        activity.GetCartListSuccessCheckout(cartitemList)
                    }
                }

            }
            .addOnFailureListener {
                Log.e("error", it.message.toString())
                when (activity) {
                    is cartlist -> {
                        activity.HideDialog()
                        activity.ShowSnackbar(it.message.toString(), false)
                    }
                }
            }

    }

    fun GetallProductCartlist(activity: Activity) {
        myFirestore.collection(ConstVal.Collection_product)
            .get()
            .addOnSuccessListener {
                val ProductList = ArrayList<ProductDataClass>()
                for (i in it.documents) {
                    val product = i.toObject(ProductDataClass::class.java)
                    product?.product_id = i.id
                    ProductList.add(product!!)

                }
                when (activity) {
                    is cartlist -> {
                        activity.SuccessGetAllProduct(productList = ProductList)
                    }
                    is Checkout -> {
                        activity.successGetallProduct(ProductList)
                    }
                }


            }
            .addOnFailureListener {
                when (activity) {
                    is cartlist -> {
                        activity.HideDialog()
                    }
                }

            }
    }

    fun DeleteCartItem(activity: Context, cartid: String) {
        myFirestore.collection(ConstVal.collection_cart)
            .document(cartid)
            .delete()
            .addOnSuccessListener {

                when (activity) {
                    is cartlist -> {
                        activity.SuccessDeleteCartItem()

                    }
                }
            }
            .addOnFailureListener {
                when (activity) {
                    is cartlist -> {
                        activity.HideDialog()
                        activity.ShowSnackbar(it.message.toString(), false)
                    }
                }
            }
    }

    fun UpdateDetailCart(cartid: String, context: Context, hashMap: HashMap<String, Any>) {
        myFirestore.collection(ConstVal.collection_cart)
            .document(cartid)
            .update(hashMap)
            .addOnSuccessListener {
                when (context) {
                    is cartlist -> {
                        context.SuccessUpdateCart()
                    }
                }

            }
            .addOnFailureListener {

                when (context) {
                    is cartlist -> {
                        context.HideDialog()
                        context.ShowSnackbar(it.message.toString(), false)

                    }
                }
            }

    }

    fun addAddressToFireStore(activity: AddAddress, address: AddressDataClass) {
        myFirestore.collection(ConstVal.collection_address)
            .document()
            .set(address, SetOptions.merge())
            .addOnCompleteListener {
                it.addOnSuccessListener {
                    activity.HideDialog()
                    activity.successAddAddress()
                }
            }
            .addOnFailureListener {
                activity.HideDialog()
                activity.failedAddAdress()
            }
    }

    fun GetAllAdressOwn(activity: Activity) {
        myFirestore.collection(ConstVal.collection_address)
            .whereEqualTo(ConstVal.user_id, GetCurrentUserID())
            .get()
            .addOnCompleteListener {
                it.addOnSuccessListener { address ->
                    val MyAddresslist: ArrayList<AddressDataClass> = ArrayList()
                    for (i in address.documents) {
                        val MyAddress = i.toObject(AddressDataClass::class.java)
                        MyAddress?.address_id = i.id
                        MyAddresslist.add(MyAddress!!)
                    }
                    when (activity) {
                        is Address -> {
                            activity.successGetMyAddressFromFireStore(MyAddresslist)
                        }
                    }

                }
            }
            .addOnFailureListener {
                when (activity) {
                    is Address -> {
                        activity.HideDialog()
                    }
                }
            }
    }

    fun UpdateAddressDetail(activity: AddAddress, dataClass: AddressDataClass, addressid: String) {
        myFirestore.collection(ConstVal.collection_address)
            .document(addressid)
            .set(dataClass).addOnSuccessListener {
                activity.HideDialog()
                activity.successUpdate()
            }
            .addOnFailureListener {
                activity.HideDialog()
                activity.failed()
            }

    }

    fun deleteAdress(activity: Address, addressid: String) {
        myFirestore.collection(ConstVal.collection_address)
            .document(addressid)
            .delete().addOnSuccessListener {
                activity.successDelete()
            }
            .addOnFailureListener {
                activity.failedDelete()
            }
    }

    fun CreateOrderCollection(activity: Activity, orderDataClass: OrderDataClass) {
        myFirestore.collection(ConstVal.Collection_Order)
            .document()
            .set(orderDataClass, SetOptions.merge())
            .addOnCompleteListener {
                it.addOnSuccessListener {
                    when (activity) {
                        is Checkout -> {
                            activity.HideDialog()
                            activity.successAddOrder()
                        }
                    }

                }
            }
            .addOnFailureListener {
                when (activity) {
                    is Checkout -> {
                        activity.HideDialog()
                        activity.FailaddOrder()
                    }
                }
            }
    }

    fun updateProductDetaiAfterOrder(
        activity: Activity,
        arrayList: ArrayList<CartDataClass>,
        orderDataClass: OrderDataClass
    ) {
        val writeBatch = myFirestore.batch()
        for (item in arrayList) {
            // var newProductQuantityAfterOrder=HashMap<String,Any>()
            //newProductQuantityAfterOrder[ConstVal.productquantity_doc]=item.productQuantity - item.card_quantity
            val sold_obj = SoldDataClass(
                item.product_Id,
                item.title,
                item.price.toString(),
                item.card_quantity.toString(),
                orderDataClass.cartImage,
                orderDataClass.orderDate,
                orderDataClass.subtotal,
                orderDataClass.shipingCharge,
                orderDataClass.totalAmount,
                orderDataClass.address_buyer,
                item.User_id_Seller

            )
            val documnetRefrences = myFirestore.collection(ConstVal.Collection_SoldProduct)
                .document(item.User_id_Seller)
            writeBatch.set(documnetRefrences, sold_obj)
        }
        for (item in arrayList) {
            val documentref = myFirestore.collection(ConstVal.collection_cart)
                .document(item.cart_id)
            writeBatch.delete(documentref)
        }
        writeBatch.commit().addOnSuccessListener {
            when (activity) {
                is Checkout -> {
                    activity.HideDialog()
                    activity.successUpdateDetailProductAfterOrder()
                }
            }
        }
            .addOnFailureListener {
                when (activity) {
                    is Checkout -> {
                        activity.HideDialog()
                        activity.failedUpdateDetailProductAfterOrder()

                    }
                }
            }

    }

    fun getAllorder(fragment: Order) {
        myFirestore.collection(ConstVal.Collection_Order)
            .whereEqualTo(ConstVal.user_id_buyer, GetCurrentUserID())
            .get()

            .addOnSuccessListener { order ->
                val Myorderlist: ArrayList<OrderDataClass> = ArrayList()
                for (i in order.documents) {
                    val Myorder = i.toObject(OrderDataClass::class.java)
                    Myorder?.order_id = i.id
                    Myorderlist.add(Myorder!!)
                }
                fragment.successGetAllOrder(Myorderlist)
            }
            .addOnFailureListener {
                fragment.failedGetAllorder()
            }
    }

    fun GetAllSoldMyProduct(soldfragment: sold) {
        myFirestore.collection(ConstVal.Collection_SoldProduct)
            .whereEqualTo(ConstVal.user_id_seller, GetCurrentUserID())
            .get().addOnSuccessListener {
                val myproductSoldList = ArrayList<SoldDataClass>()
                for (i in it) {
                    val soldOneitem = i.toObject(SoldDataClass::class.java)
                    soldOneitem.order_id = i.id
                    myproductSoldList.add(soldOneitem)
                }
                soldfragment.successgetAllproductSold(myproductSoldList)


            }
            .addOnFailureListener {
                soldfragment.failedgetAllsoldOwnList()
            }
    }


}






