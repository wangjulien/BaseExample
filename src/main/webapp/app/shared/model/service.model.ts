import { Status } from 'app/shared/model/enumerations/status.model';

export interface IService {
  id?: number;
  serviceName?: string;
  status?: Status;
  hostId?: number;
}

export const defaultValue: Readonly<IService> = {};
