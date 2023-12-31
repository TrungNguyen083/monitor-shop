import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Cart } from 'src/app/common/Cart';
import { CartDetail } from 'src/app/common/CartDetail';
import { Customer } from 'src/app/common/Customer';
import { Order } from 'src/app/common/Order';
import { OrderDetail } from 'src/app/common/OrderDetail';
import { CartService } from 'src/app/services/cart.service';
import { LocalStorageService } from 'src/app/services/local-storage.service';
import { OrdersService } from 'src/app/services/orders.service';
import { SendmailService } from 'src/app/services/sendmail.service';
import { SharedDataService } from 'src/app/services/shared-data.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-carts',
  templateUrl: './carts.component.html',
  styleUrls: ['./carts.component.css']
})
export class CartsComponent implements OnInit {

  cart!: Cart;
  cartDetail!: CartDetail;
  cartDetails!: CartDetail[];
  order!: Order;
  orderDetail!: OrderDetail;
  totalItem!: number;
  totalCartItem!: number;
  customerId!: number;
  amount!: number;
  discount!: number;
  amountReal!: number;
  postForm: FormGroup;

  user!: Customer;

  constructor(
    private cartService: CartService,
    private toastr: ToastrService,
    private orderService: OrdersService,
    private localStorageService: LocalStorageService,
    private router: Router,
    private sendMailService: SendmailService,
    private sharedDataService: SharedDataService,
  ) {
    this.postForm = new FormGroup({
      'address': new FormControl(null, [Validators.required, Validators.minLength(3)]),
      'phone': new FormControl(null, [Validators.required])
    })
  }

  ngOnInit(): void {
    this.checkLogin();
    this.cartService.$data.subscribe(data => {
      this.totalCartItem = data;
    })
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
      this.postForm = new FormGroup({
        'address': new FormControl(this.cart.address, [Validators.required, Validators.minLength(3)]),
        'phone': new FormControl(this.cart.phone, [Validators.required])
      })
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

  delete(id: number) {
    Swal.fire({
      title: 'Bạn muốn xoá sản phẩm này ra khỏi giỏ hàng?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      cancelButtonText: 'Không',
      confirmButtonText: 'Xoá'
    }).then((result) => {
      if (result.isConfirmed) {
        this.cartService.deleteDetail(id).subscribe(data => {
          this.toastr.success('Xoá thành công!', 'Hệ thống');
          this.ngOnInit();
        }, error => {
          this.toastr.error('Xoá thất bại! ' + error.status, 'Hệ thống');
        })
      }
    })

  }

  update(id: number, quantity: number) {
    if (quantity < 1) {
      this.delete(id);
    } else {
      this.cartService.getOneDetail(id).subscribe(data => {
        this.cartDetail = data as CartDetail;
        this.cartDetail.quantity = quantity;
        this.cartDetail.price = (this.cartDetail.product.price * (1 - this.cartDetail.product.discount / 100)) * quantity;
        this.cartService.updateDetail(this.cartDetail).subscribe(data => {
          this.ngOnInit();
        }, error => {
          this.toastr.error('Lỗi!' + error.status, 'Hệ thống');
        })
      }, error => {
        this.toastr.error('Lỗi! ' + error.status, 'Hệ thống');
      })
    }
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

  checkOut() {
    Swal.fire({
      title: 'Bạn có muốn đặt đơn hàng này?',
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      cancelButtonText: 'Không',
      confirmButtonText: 'Đặt'
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.fire({
          title: 'Bạn muốn chọn hình thức thanh toán nào?',
          icon: 'question',
          showCancelButton: true,
          showDenyButton: true,
          confirmButtonColor: '#3085d6',
          cancelButtonColor: '#d33',
          confirmButtonText: 'Thanh toán khi nhận hàng',
          denyButtonText: `Thanh toán online`,
          cancelButtonText: 'Hủy',
        }).then((result) => {
          if (result.isConfirmed) {
            this.handleCheckout();
          } else if (result.isDenied) {
            this.sharedDataService.setAmount(this.amount);
            this.sharedDataService.setAddress(this.postForm.value.address)
            this.sharedDataService.setPhone(this.postForm.value.phone)
            console.log("Address:", this.postForm.value.address)
            console.log("Phone:", this.postForm.value.phone)
            this.router.navigate(['/payment']);
          }
        })
      }
    })
  }


  handleCheckout() {
    this.order = new Order(0, this.amount, this.postForm.value.address, this.postForm.value.phone, new Date(), 1, new Customer(this.customerId));
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
              'Bạn đã đặt hàng thành công.',
              'success'
            )
  }
}
