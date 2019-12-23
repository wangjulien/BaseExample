export interface IView {
  id?: number;
  title?: string;
  isLocked?: boolean;
}

export const defaultValue: Readonly<IView> = {
  isLocked: false
};
