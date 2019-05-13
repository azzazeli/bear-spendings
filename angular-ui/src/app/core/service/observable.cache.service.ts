import { Observable } from 'rxjs';

export abstract class ObservableCacheService<T> {
  //todo: try RequestCache https://blog.fullstacktraining.com/caching-http-requests-with-angular/
  protected observableCache: {[key: number]: Observable<T>} = {};

  protected abstract fetchObservable(id: number): Observable<T>;

  public getObservableById(id: number): Observable<T> {
    if(!this.observableCache[id]) {
      this.observableCache[id] = this.fetchObservable(id);
    }
    return this.observableCache[id];
  }

}
