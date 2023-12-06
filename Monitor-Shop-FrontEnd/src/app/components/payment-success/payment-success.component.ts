import { Component, OnInit } from '@angular/core';
import { Order } from 'src/app/common/Order';
import { PaymentService } from 'src/app/services/payment.service';
import { ActivatedRoute } from '@angular/router';
import { CartDetail } from 'src/app/common/CartDetail';
import { Customer } from 'src/app/common/Customer';
import { OrderDetail } from 'src/app/common/OrderDetail';
import { Cart } from 'src/app/common/cart';
import { CartService } from 'src/app/services/cart.service';
import { LocalStorageService } from 'src/app/services/local-storage.service';
import { OrdersService } from 'src/app/services/orders.service';
import Swal from 'sweetalert2';

@Component({
    selector: 'app-payment-success',
    templateUrl: './payment-success.component.html',
    styleUrls: ['./payment-success.component.css']
})
export class PaymentSuccessComponent implements OnInit {

    paymentId!: any;
    payerId!: any;
    cart!: Cart;
    cartDetail!: CartDetail;
    cartDetails!: CartDetail[];
    order!: Order;
    orderDetail!: OrderDetail;
    customerId!: number;
    user!: Customer;
    totalItem!: number;
    totalCartItem!: number;
    amount!: number;
    discount!: number;
    amountReal!: number;
    router: any;
    toastr: any;
    address!: string;
    phone!: string;

    constructor(
        private paymentService: PaymentService,
        private route: ActivatedRoute,
        private cartService: CartService,
        private orderService: OrdersService,
        private localStorageService: LocalStorageService,
    ) {
    }

    ngOnInit(): void {
        this.checkLogin();
        console.log(this.user.address)
        this.paymentId = this.route.snapshot.queryParamMap.get('paymentId');
        this.payerId = this.route.snapshot.queryParamMap.get('PayerID');
        this.success();
    }

    checkLogin() {
        this.user = this.localStorageService.getUser();
        if (this.user != null) {
            this.customerId = this.user.userId;
            this.amount = 0;
            this.discount = 0;
            this.amountReal = 0;
            this.getAllItem();
        } else {
            this.router.navigate(['/login']);
        }
    }

    getAllItem() {
        this.cartService.getCart(this.customerId).subscribe(data => {
            this.cart = data as Cart;
            this.cartService.getAllDetail(this.cart.id).subscribe(data => {
                this.cartDetails = data as CartDetail[];
                this.totalItem = this.cartDetails.length;
                this.cartService.setData(this.totalItem);
                this.cartDetails.forEach(item => {
                    this.amountReal += item.product.price * item.quantity;
                    this.amount += item.price;
                })
                this.discount = this.amountReal - this.amount;
                this.cart.amount = this.amount;
                this.cartService.updateCart(this.customerId, this.cart)
            }, error => {
                this.toastr.error('Lỗi truy xuất dữ liệu!' + error.status, 'Hệ thống');
            })
        }, error => {
            this.toastr.error('Lỗi truy xuất dữ liệu!' + error.status, 'Hệ thống');
        })
    }

    removeAllItem() {
        this.cartService.getCart(this.customerId).subscribe(data => {
            this.cart = data as Cart;
            this.cartService.getAllDetail(this.cart.id).subscribe(data => {
                this.cartDetails = data as CartDetail[];
                this.cartDetails.forEach(item => {
                    this.cartService.deleteDetail(item.id).subscribe(data => {
                        this.ngOnInit();
                    }, error => {
                        this.toastr.error('Lỗi! ' + error.status, 'Hệ thống');
                    })
                })
            }, error => {
                this.toastr.error('Lỗi! ' + error.status, 'Hệ thống');
            })
        }, error => {
            this.toastr.error('Lỗi! ' + error.status, 'Hệ thống');
        })
    }

    handleCheckout() {
        this.order = new Order(0, this.amount, this.user.address, this.user.phone, new Date(), 1, new Customer(this.customerId));
        this.orderService.checkOut(this.order).subscribe(data => {
            this.order = data as Order;
            //chuyen vao order detail
            this.cartDetails.forEach(item => {
                this.orderDetail = new OrderDetail(0, item.quantity, item.price, item.product, this.order);
                this.orderService.saveOrderDetail(this.orderDetail).subscribe(data => {
                    console.log('done')
                })
            })
        }, error => {
            this.toastr.error('Lỗi! ' + error.status, 'Hệ thống');
        })
        this.removeAllItem();
        Swal.fire(
            'Thành công!',
            'Bạn đã đặt hàng thành công và thanh toán thành công.',
            'success'
        )
    }

    success() {
        this.paymentService.success(this.paymentId, this.payerId).subscribe(data => {
            this.handleCheckout();
        })
    }
}
