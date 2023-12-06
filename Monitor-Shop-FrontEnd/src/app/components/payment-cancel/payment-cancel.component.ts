import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Payment } from 'src/app/common/Payment';
import { Cart } from 'src/app/common/cart';

@Component({
    selector: 'app-payment-cancel',
    templateUrl: './payment-cancel.component.html',
    styleUrls: ['./payment-cancel.component.css']
})
export class PaymentCancleComponent implements OnInit {

    payment!: Payment;
    cart!:Cart;

    constructor() {
    }

    ngOnInit(): void {
    }
}
