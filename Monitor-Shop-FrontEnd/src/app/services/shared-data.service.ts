import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SharedDataService {
  private amountSubject = new BehaviorSubject<number>(0);
  private addressSubjject = new BehaviorSubject<string>("");
  private phoneSubjject = new BehaviorSubject<string>("");
  amount$ = this.amountSubject.asObservable();
  address$ = this.addressSubjject.asObservable();
  phone$ = this.phoneSubjject.asObservable();
  setAmount(amount: number) {
    this.amountSubject.next(amount);
  }
  setAddress(address: string) {
    this.addressSubjject.next(address);
  }
  setPhone(phone: string) {
    this.phoneSubjject.next(phone);
  }

}