import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Payment } from 'src/app/common/Payment';
import { Cart } from 'src/app/common/cart';
import { RouterModule } from '@angular/router';

@Component({
    selector: 'app-payment-cancle',
    templateUrl: './payment-cancle.component.html',
    styleUrls: ['./payment-cancle.component.css']
})
export class PaymentCancleComponent implements OnInit {

    payment!: Payment;
    cart!:Cart;

    @Output()
    saveFinish: EventEmitter<any> = new EventEmitter<any>();

    constructor() {
    }

    ngOnInit(): void {
    }
}
