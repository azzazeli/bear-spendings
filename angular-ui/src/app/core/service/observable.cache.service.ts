import {Observable} from 'rxjs';
import {shareReplay} from "rxjs/operators";
import {NGXLogger} from "ngx-logger";

export abstract class ObservableCacheService<T> {
  protected observableCache: {[key: number]: Observable<T>} = {};

  protected constructor(protected logger: NGXLogger) { }

  protected abstract fetchObservable(id: number): Observable<T>;

  public getObservableById(id: number): Observable<T> {
    if(!this.observableCache[id]) {
      this.logger.debug(`observable for id:${id} not present in cash`);
      this.observableCache[id] = this.fetchObservable(id).pipe(shareReplay(1));
    }
    return this.observableCache[id];
  }

}
