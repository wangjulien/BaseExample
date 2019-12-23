import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IView, defaultValue } from 'app/shared/model/view.model';

export const ACTION_TYPES = {
  FETCH_VIEW_LIST: 'view/FETCH_VIEW_LIST',
  FETCH_VIEW: 'view/FETCH_VIEW',
  CREATE_VIEW: 'view/CREATE_VIEW',
  UPDATE_VIEW: 'view/UPDATE_VIEW',
  DELETE_VIEW: 'view/DELETE_VIEW',
  RESET: 'view/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IView>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ViewState = Readonly<typeof initialState>;

// Reducer

export default (state: ViewState = initialState, action): ViewState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_VIEW_LIST):
    case REQUEST(ACTION_TYPES.FETCH_VIEW):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_VIEW):
    case REQUEST(ACTION_TYPES.UPDATE_VIEW):
    case REQUEST(ACTION_TYPES.DELETE_VIEW):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_VIEW_LIST):
    case FAILURE(ACTION_TYPES.FETCH_VIEW):
    case FAILURE(ACTION_TYPES.CREATE_VIEW):
    case FAILURE(ACTION_TYPES.UPDATE_VIEW):
    case FAILURE(ACTION_TYPES.DELETE_VIEW):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_VIEW_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_VIEW):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_VIEW):
    case SUCCESS(ACTION_TYPES.UPDATE_VIEW):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_VIEW):
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

const apiUrl = 'api/views';

// Actions

export const getEntities: ICrudGetAllAction<IView> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_VIEW_LIST,
    payload: axios.get<IView>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IView> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_VIEW,
    payload: axios.get<IView>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IView> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_VIEW,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IView> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_VIEW,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IView> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_VIEW,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
