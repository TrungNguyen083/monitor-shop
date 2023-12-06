import { Injectable } from '@angular/core';
import { Customer } from '../common/Customer';

@Injectable({
  providedIn: 'root'
})
export class LocalStorageService {

  user!:Customer;
  data!:any;
  amount!: Number;

  constructor() { }

  saveLogin(user:Customer) {
    window.localStorage.removeItem("login");
    window.localStorage.setItem("login", JSON.stringify(user));
  }

  saveAmount(amount: Number) {
    this.removeAmount();
    window.localStorage.setItem("amount", JSON.stringify(amount));
  }

  getAmount() {
    this.data = localStorage.getItem("login");
    return JSON.parse(this.data);
  }

  removeAmount() {
    window.localStorage.removeItem("amount");
  }

  getUser() {
    this.data = localStorage.getItem("login");
    return JSON.parse(this.data);
  }

  logout() {
    window.localStorage.removeItem("login");
  }
}
