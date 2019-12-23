import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import View from './view';
import ViewDetail from './view-detail';
import ViewUpdate from './view-update';
import ViewDeleteDialog from './view-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ViewUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ViewUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ViewDetail} />
      <ErrorBoundaryRoute path={match.url} component={View} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ViewDeleteDialog} />
  </>
);

export default Routes;
