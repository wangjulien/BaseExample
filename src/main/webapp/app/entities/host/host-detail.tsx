import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './host.reducer';
import { IHost } from 'app/shared/model/host.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IHostDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class HostDetail extends React.Component<IHostDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { hostEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Host [<b>{hostEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="hostName">Host Name</span>
            </dt>
            <dd>{hostEntity.hostName}</dd>
            <dt>
              <span id="status">Status</span>
            </dt>
            <dd>{hostEntity.status}</dd>
          </dl>
          <Button tag={Link} to="/host" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/host/${hostEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ host }: IRootState) => ({
  hostEntity: host.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(HostDetail);
