import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './view.reducer';
import { IView } from 'app/shared/model/view.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IViewDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ViewDetail extends React.Component<IViewDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { viewEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            View [<b>{viewEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="title">Title</span>
            </dt>
            <dd>{viewEntity.title}</dd>
            <dt>
              <span id="isLocked">Is Locked</span>
            </dt>
            <dd>{viewEntity.isLocked ? 'true' : 'false'}</dd>
          </dl>
          <Button tag={Link} to="/view" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/view/${viewEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ view }: IRootState) => ({
  viewEntity: view.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ViewDetail);
