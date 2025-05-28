import type { Option } from './option';

export type SKU = {
    id: string;
    name: string;
    costPrice: number;
    amountAvailable: number;
    marginPercentage: number;
    option: Option;
    createdAt: Date;
    updatedAt: Date;
}