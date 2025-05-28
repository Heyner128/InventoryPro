import type { Option } from './option';

export type Item = {
    skuId: string;
    skuName: string;
    brand: string;
    costPrice: number;
    amountAvailable: number;
    marginPercentage: number;
    option: Option;
    quantity: number;
    createdAt: Date;
    updatedAt: Date;
}