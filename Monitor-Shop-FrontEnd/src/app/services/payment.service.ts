import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Payment } from '../common/Payment'

@Injectable({
    providedIn: 'root'
})
export class PaymentService {
    url = 'http://localhost:8989/api/payment';
    constructor(private httpClient: HttpClient) { }

    payment(payment: Payment) {
        return this.httpClient.post(this.url, payment, { responseType: 'text'});
    }

    cancle() {
        return this.httpClient.get(this.url + '/cancle');
    }

    success(paymentId: string, payerId: string) {
        return this.httpClient.get(`${this.url}/success?paymentId=${paymentId}&payerId=${payerId}`);
    }
}
