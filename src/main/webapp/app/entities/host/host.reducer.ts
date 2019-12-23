import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IHost, defaultValue } from 'app/shared/model/host.model';

export const ACTION_TYPES = {
  FETCH_HOST_LIST: 'host/FETCH_HOST_LIST',
  FETCH_HOST: 'host/FETCH_HOST',
  CREATE_HOST: 'host/CREATE_HOST',
  UPDATE_HOST: 'host/UPDATE_HOST',
  DELETE_HOST: 'host/DELETE_HOST',
  RESET: 'host/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IHost>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type HostState = Readonly<typeof initialState>;

// Reducer

export default (state: HostState = initialState, action): HostState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_HOST_LIST):
    case REQUEST(ACTION_TYPES.FETCH_HOST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_HOST):
    case REQUEST(ACTION_TYPES.UPDATE_HOST):
    case REQUEST(ACTION_TYPES.DELETE_HOST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_HOST_LIST):
    case FAILURE(ACTION_TYPES.FETCH_HOST):
    case FAILURE(ACTION_TYPES.CREATE_HOST):
    case FAILURE(ACTION_TYPES.UPDATE_HOST):
    case FAILURE(ACTION_TYPES.DELETE_HOST):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_HOST_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_HOST):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_HOST):
    case SUCCESS(ACTION_TYPES.UPDATE_HOST):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_HOST):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/hosts';

// Actions

export const getEntities: ICrudGetAllAction<IHost> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_HOST_LIST,
    payload: axios.get<IHost>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IHost> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_HOST,
    payload: axios.get<IHost>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IHost> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_HOST,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IHost> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_HOST,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IHost> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_HOST,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
