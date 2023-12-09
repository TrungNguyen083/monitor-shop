import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RateService {

  url = 'http://monitorshop.onrender.com/api/rates';

  constructor(private httpClient: HttpClient) { }

  getAllRate() {
    return this.httpClient.get(this.url);
  }

  delete(id:number) {
    return this.httpClient.delete(this.url+'/'+id);
  }
}
