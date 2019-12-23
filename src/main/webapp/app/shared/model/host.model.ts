import { IService } from 'app/shared/model/service.model';
import { Status } from 'app/shared/model/enumerations/status.model';

export interface IHost {
  id?: number;
  hostName?: string;
  status?: Status;
  services?: IService[];
}

export const defaultValue: Readonly<IHost> = {};
