import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Host from './host';
import HostDetail from './host-detail';
import HostUpdate from './host-update';
import HostDeleteDialog from './host-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={HostUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={HostUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={HostDetail} />
      <ErrorBoundaryRoute path={match.url} component={Host} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={HostDeleteDialog} />
  </>
);

export default Routes;
