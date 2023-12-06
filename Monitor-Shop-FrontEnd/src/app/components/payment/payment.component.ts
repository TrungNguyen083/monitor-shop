import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { Payment } from 'src/app/common/Payment';
import { PaymentService } from 'src/app/services/payment.service';
import { SharedDataService } from 'src/app/services/shared-data.service';

@Component({
    selector: 'app-payment',
    templateUrl: './payment.component.html',
    styleUrls: ['./payment.component.css']
})
export class PaymentComponent implements OnInit {

    postForm: FormGroup;
    payment!: Payment;
    amount!: Number;
    formattedAmount!: any
    address!: any

    constructor(
        private paymentService: PaymentService,
        private toastr: ToastrService,
        private sharedDataService: SharedDataService,
    ) {
        this.sharedDataService.amount$.subscribe(amount => {
            this.amount = amount;
            const exchangeRate = 23000;
            this.amount = Math.round((amount / exchangeRate) * 100) / 100;
        });
        this.sharedDataService.address$.subscribe(address => {
            this.address = address;
            console.log(this.address)
        })
        
        this.postForm = new FormGroup({
            'price': new FormControl(this.amount),
            'currency': new FormControl(null, Validators.required),
            'method': new FormControl("Paypal", Validators.required),
            'intent': new FormControl("sale", Validators.required),
            'description': new FormControl(null, Validators.required),
        })
    }

    ngOnInit(): void {

    }

    createPayment() {
        if (this.postForm.valid) {
            this.payment = this.postForm.value;

            this.paymentService.payment(this.payment).subscribe(data => {
                this.sharedDataService.setAddress(this.address);
                window.location.href = data.toString();

            }, error => {
                this.toastr.error('Không hợp lệ, vui lòng thử lại! ' + error.status, 'Hệ thống');
            })
        } else {
            this.toastr.error('Hãy kiểm tra và nhập lại dữ liệu!', 'Hệ thống');
        }
    }
}
